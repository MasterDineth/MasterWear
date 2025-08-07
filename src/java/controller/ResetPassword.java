package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.EmailBody;
import model.Generators;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Gson gson = new Gson();
        JsonObject userJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        HttpSession htses = request.getSession(false);

        if (htses != null) {

            System.out.println("ses ok");

            User u = (User) htses.getAttribute("user");

            if (u.getVcode().equals("PWResetOK")) {

                if (htses.getAttribute("email") == null) {
                    responseObject.addProperty("message", "Email Not Found In The Session! Please Try Again!");
                    responseObject.addProperty("errorCode", "SES-NODATA");

                } else {

                    String email = htses.getAttribute("email").toString();
                    String password = userJsonObject.get("password").getAsString();
                    String confirmPassword = userJsonObject.get("confirmPassword").getAsString();

                    System.out.println(password);
                    System.out.println(password);

                    Validations v = new Validations();

                    if (v.isEmpty(password)) {
                        responseObject.addProperty("message", "Password Is Empty!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");

                    } else if (v.isEmpty(confirmPassword)) {
                        responseObject.addProperty("message", "Confirm Password Is Empty!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");

                    } else if (!password.equals(confirmPassword)) {
                        responseObject.addProperty("message", "Confirm Password Does Not Match!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");
                    } else {

                        SessionFactory sf = HibernateUtil.getSessionFactory();
                        Session s = sf.openSession();

                        Criteria c1 = s.createCriteria(User.class);
                        Criterion crt1 = Restrictions.eq("email", email);
                        c1.add(crt1);

                        if (c1.list().isEmpty()) {

                            responseObject.addProperty("message", "User Not Found!");
                            responseObject.addProperty("errorCode", "INV-EM");

                        } else {

                            User user = (User) c1.list().get(0);

                            user.setPassword(password);
                            user.setVcode("Verified");

                            s.save(user);
                            s.beginTransaction().commit();

                            htses.invalidate();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    LocalDateTime now = LocalDateTime.now();

                                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");

                                    String date = now.format(dateFormatter);
                                    String time = now.format(timeFormatter);
                                    String username = user.getUsername();

                                    Mail.sendMail(email, "Master Wear - Security Notice!", EmailBody.getPasswordResetEmailBody(username, date, time));

                                    System.out.println("email sent");
                                }
                            }).start();

                            responseObject.addProperty("status", true);
                            responseObject.addProperty("message", "Account Password Changed Successfully!");
                            s.close();
                        }
                    }
                }

            } else {
                responseObject.addProperty("message", "Email Not verified! Please Verify Your Account To Reset Password!");
                responseObject.addProperty("errorCode", "ACC-UNVER");
            }

            String responseText = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);

        } else {

            System.out.println("no ses");

        }
    }
}
