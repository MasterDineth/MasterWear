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
import model.Generators;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("statuss", false);

        HttpSession htses = request.getSession();

        if (htses.getAttribute("email") == null) {
            responseObject.addProperty("message", "Email Not Found In The Session!");
            responseObject.addProperty("errorCode", "SES-NODATA");

        } else {

            String email = htses.getAttribute("email").toString();

            JsonObject verification = gson.fromJson(request.getReader(), JsonObject.class);

            String vcode = verification.get("verification").getAsString();

            if (vcode.isEmpty()) {
                responseObject.addProperty("message", "Verification Code Is Empty!");
                responseObject.addProperty("errorCode", "EMP-DATA");
            }

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            System.out.println(email);
            System.out.println(vcode);

            Criteria c1 = s.createCriteria(User.class);

            Criterion crt1 = Restrictions.eq("email", email);
            Criterion crt2 = Restrictions.eq("vcode", vcode);

            c1.add(crt1);
            c1.add(crt2); 

            if (c1.list().isEmpty()) {
                System.out.println("1");
                responseObject.addProperty("message", "Invalid Verification Code!");
                responseObject.addProperty("errorCode", "INV-DATA");

            } else {
                System.out.println("2");

                if (vcode.length() == 10) {

                    System.out.println("account activation code");

                    User user = (User) c1.list().get(0);
                    user.setVcode("Verified");

                    s.update(user);
                    s.beginTransaction().commit();

                    htses.setAttribute("user", user);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("VerificationType", "ACC");
                    responseObject.addProperty("message", "Account Verification successful");

                } else if (vcode.length() == 11 || htses.getAttribute("passwordReset").equals("true")) {
                    
                    System.out.println("3");

                    System.out.println("password reset code");
                     String tempPass = Generators.generateRandomCharachers(20, true, true, true);
                    
                    User user = (User) c1.list().get(0);
                    user.setVcode("PWResetOK");
                    user.setPassword(tempPass);                    

                    s.update(user);
                    s.beginTransaction().commit(); 
                    
                    user.setPassword(null);
                    user.setUsername(null);
                    
                    htses.setAttribute("user", user);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("VerificationType", "PSW");
                    responseObject.addProperty("message", "Password Reset Verification successful");

                } else {
                    System.out.println("end if ");
                    responseObject.addProperty("message", "Invalid Verification Code!");
                    responseObject.addProperty("errorCode", "INV-DATA-PWRS");
                }
//                s.close();
            }
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
