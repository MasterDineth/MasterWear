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
import model.Generators;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import model.EmailBody;

@WebServlet(name = "UpdatePassword", urlPatterns = {"/UpdatePassword"})
public class UpdatePassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Gson gson = new Gson();
        JsonObject userJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        HttpSession htses = request.getSession(false);

        if (htses != null) {

            User u = (User) htses.getAttribute("user");

            if (u.getVcode().equals("Verified")) {

                if (htses.getAttribute("user") == null) {
                    responseObject.addProperty("message", "User Not Found In The Session! Please Try Again!");
                    responseObject.addProperty("errorCode", "SES-NODATA");

                } else {

                    String oldPassword = userJsonObject.get("oldPassword").getAsString();
                    String newPassword = userJsonObject.get("newPassword").getAsString();
                    String confirmNewPassword = userJsonObject.get("confirmNewPassword").getAsString();

                    Validations v = new Validations();

                    if (v.isEmpty(oldPassword)) {
                        responseObject.addProperty("message", "Please Enter Old Password!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");

                    } else if (v.isEmpty(newPassword)) {
                        responseObject.addProperty("message", "Please Enter New Password!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");

                    } else if (v.isEmpty(confirmNewPassword)) {
                        responseObject.addProperty("message", "Please Enter Confirm Password!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");

                    } else if (!newPassword.equals(confirmNewPassword)) {
                        responseObject.addProperty("message", "Confirm Password Does Not Match!");
                        responseObject.addProperty("errorCode", "VAL-ERROR");
                    } else {

                        SessionFactory sf = HibernateUtil.getSessionFactory();
                        Session s = sf.openSession();

                        Criteria c1 = s.createCriteria(User.class);
                        Criterion crt1 = Restrictions.eq("email", u.getEmail());
                        Criterion crt2 = Restrictions.eq("password", oldPassword);
                        c1.add(crt1);
                        c1.add(crt2);

                        if (c1.list().isEmpty()) {

                            responseObject.addProperty("message", "Old Password Does Not Match!");
                            responseObject.addProperty("errorCode", "INV-EM");

                        } else {

                            User user = (User) c1.list().get(0);

                            user.setPassword(newPassword);

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

                                    Mail.sendMail(u.getEmail(), "Master Wear - Security Notice!", EmailBody.getPasswordResetEmailBody(username, date, time));

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
