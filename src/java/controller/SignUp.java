package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject userJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String username = userJsonObject.get("username").getAsString();
        String email = userJsonObject.get("email").getAsString();
        String password = userJsonObject.get("password").getAsString();
        String confirmPassword = userJsonObject.get("confirmPassword").getAsString();

        Validations v = new Validations();

        if (v.isEmpty(username)) {
            responseObject.addProperty("message", "Empty Username!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (!v.charLen(username, 50)) {
            responseObject.addProperty("message", "Username Exeed Allowed Character Limit!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (v.isEmpty(email)) {
            responseObject.addProperty("message", "Empty Eamil Address!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (!v.charLen(email, 100)) {
            responseObject.addProperty("message", "Eamil Address Exeed Allowed Character Limit!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (v.validateEmail(email)) {
            responseObject.addProperty("message", "Invalid Eamil Address!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (v.isEmpty(password)) {
            responseObject.addProperty("message", "Password Is Empty!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (!v.charLen(email, 40)) {
            responseObject.addProperty("message", "Password Exeed Allowed Character Limit!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (!password.equals(confirmPassword)) {
            responseObject.addProperty("message", "Confirm Password Does Not Match!");
            responseObject.addProperty("errorCode", "VAL-ERROR");
        } else {

            String vcode = Generators.generateRandomCharachers(10, false, true, false);

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            User user = new User();

            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setVcode(vcode);

            Criteria c = s.createCriteria(User.class);
            Criterion crt1 = Restrictions.eq("email", email);

            c.add(crt1);

            if (c.list().isEmpty()) {

                s.save(user);
                s.beginTransaction().commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "Master Wear - Validate Email Address ", EmailBody.getVerificationCodeEmailBody(vcode));
                    }
                }).start();

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Account Registered Successfully!");

                System.out.println("email sent");

            } else {
                responseObject.addProperty("message", "Email Address " + email + " Already Exists!");
                responseObject.addProperty("errorCode", "SIGNIN-ERROR");
            }

            HttpSession ses = request.getSession();
            ses.setAttribute("email", email);

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }
}
