package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet(name = "LoadUserData", urlPatterns = {"/LoadUserData"})
public class LoadUserData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ok");

        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("user") != null) {

            User user = (User) ses.getAttribute("user");
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("status", false);

            responseObject.addProperty("username", user.getUsername());
            responseObject.addProperty("email", user.getEmail());

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            int userId = user.getId();

            UserData ud = (UserData) s.get(UserData.class, userId);

            if (ud != null) {

                String firstName = ud.getFirstName();
                String lastName = ud.getLastName();
                String mobile = ud.getMobile();
                String bio = ud.getBio();
                String line1 = ud.getAddress().getLine1();
                String line2 = ud.getAddress().getLine2();
                String postalCode = ud.getAddress().getPostalCode();
                int cityId = ud.getAddress().getCity().getId();
                String city = ud.getAddress().getCity().getName();

                responseObject.addProperty("firstName", firstName);
                responseObject.addProperty("lastName", lastName);
                responseObject.addProperty("bio", bio);
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

            //get other user data from database
//            String  year = new SimpleDateFormat("MMM yyyy").format(user.getCreated_at());
//            responeObject.addProperty("since", year);


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
