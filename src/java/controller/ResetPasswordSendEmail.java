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

@WebServlet(name = "ResetPasswordSendEmail", urlPatterns = {"/ResetPasswordSendEmail"})
public class ResetPasswordSendEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject signIn = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String email = signIn.get("email").getAsString();

        //validations
        Validations v = new Validations();

        if (v.isEmpty(email)) {
            responseObject.addProperty("message", "Empty Email Address!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(User.class);

            Criterion crt1 = Restrictions.eq("email", email);

            c1.add(crt1);

            if (c1.list().isEmpty()) {
                responseObject.addProperty("message", "Email Not Found!");
                responseObject.addProperty("errorCode", "INV-CREDENT");

            } else {

                String vcode = Generators.generateRandomCharachers(11, false, true, false);
                HttpSession htses = request.getSession();

                htses.setAttribute("email", email);

                User user = (User) c1.list().get(0);
                user.setVcode(vcode);

                s.update(user);
                s.beginTransaction().commit();

                System.out.println("user verification code updated!");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "Master Wear - Reset Password ", EmailBody.getVerificationCodeEmailBody(vcode));
                    }
                }).start();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Email Send Successfully!");

                HttpSession ses = request.getSession();
                ses.setAttribute("email", email);

                ses.setAttribute("passwordReset", "true");

                System.out.println(ses.getAttribute("email"));
                s.close();
            }
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
