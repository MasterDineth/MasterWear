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
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject signIn = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String email = signIn.get("email").getAsString();
        String password = signIn.get("password").getAsString();

        System.out.println("ok");

        //validations
        Validations v = new Validations();

        if (v.isEmpty(email)) {
            responseObject.addProperty("message", "Empty Email Address!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else if (v.isEmpty(password)) {
            responseObject.addProperty("message", "Empty Password!");
            responseObject.addProperty("errorCode", "VAL-ERROR");

        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(User.class);

            Criterion crt1 = Restrictions.eq("email", email);
            Criterion crt2 = Restrictions.eq("password", password);

            c1.add(crt1);
            c1.add(crt2);

            if (c1.list().isEmpty()) {
                responseObject.addProperty("message", "Invalid credentials");
                responseObject.addProperty("errorCode", "INV-CREDENT");

            } else {
                User user = (User) c1.uniqueResult();
                HttpSession ses = request.getSession();

                if (!user.getVcode().equals("Verified")) { // unverified user

                    ses.setAttribute("email", email);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Account Is Not Verified!");
                    responseObject.addProperty("errorCode", "ACC-!VER");
                    responseObject.addProperty("loginStatus", "0");
                } else { //verified user

                    user.setPassword("");
                    ses.setAttribute("user", user);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "success");
                    responseObject.addProperty("loginStatus", "1");
                }
            }
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
