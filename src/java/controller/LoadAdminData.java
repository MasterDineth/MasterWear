package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Admin;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserData;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@WebServlet(name = "LoadAdminData", urlPatterns = {"/LoadAdminData"})
public class LoadAdminData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ok");

        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("admin") != null) {

            Admin sesAdmin = (Admin) ses.getAttribute("admin");
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("status", false);

            responseObject.addProperty("username", sesAdmin.getUsername());
            responseObject.addProperty("email", sesAdmin.getEmail());

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            int adminId = sesAdmin.getId();

            Admin admin = (Admin) s.get(Admin.class, adminId);

            if (admin != null) {

                String email = admin.getEmail();
                String username = admin.getUsername();
                String firstName = admin.getFirstName();
                String lastName = admin.getLastName();
                String mobile = admin.getMobile();
                String line1 = admin.getLine1();
                String line2 = admin.getLine2();
                String postalCode = admin.getZip();
                int cityId = admin.getCity().getId();
                String city = admin.getCity().getName();

                responseObject.addProperty("email", email);
                responseObject.addProperty("username", username);
                responseObject.addProperty("firstName", firstName);
                responseObject.addProperty("lastName", lastName);
                responseObject.addProperty("mobile", mobile);
                responseObject.addProperty("line1", line1);
                responseObject.addProperty("line2", line2);
                responseObject.addProperty("postalCode", postalCode);
                responseObject.addProperty("cityId", cityId);
                responseObject.addProperty("city", city);

                responseObject.addProperty("hasUserData", true);

            } else {
                System.out.println("no userdata");

                responseObject.addProperty("hasUserData", false);

            }
            responseObject.addProperty("status", true);
            Gson gson = new Gson();

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);

        } else {
            System.out.println("no Session");
        }

    }

}
