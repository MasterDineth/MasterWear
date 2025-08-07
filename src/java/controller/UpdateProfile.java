package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserData;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "UpdateProfile", urlPatterns = {"/UpdateProfile"})
public class UpdateProfile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession htses = request.getSession(false);

        System.out.println("request reseved");

        JsonObject responseObject = new JsonObject();
        Gson gson = new Gson();
        responseObject.addProperty("status", false);

        if (htses.getAttribute("user") == null) {
            responseObject.addProperty("message", "User Session Not Found In The Session!");
            responseObject.addProperty("errorCode", "SES-NODATA");

        } else {

            User user = (User) htses.getAttribute("user");

            System.out.println("ok");

            JsonObject userJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

            String firstName = userJsonObject.get("firstName").getAsString();
            String lastName = userJsonObject.get("lastName").getAsString();
            String mobile = userJsonObject.get("mobile").getAsString();
            String line1 = userJsonObject.get("line1").getAsString();
            String line2 = userJsonObject.get("line2").getAsString();
            String city = userJsonObject.get("city").getAsString();
            String postalCode = userJsonObject.get("postalCode").getAsString();

            int cityId = Integer.parseInt(city);

            Validations v = new Validations();

            // validate profile data
            if (v.isEmpty(firstName)) {
                responseObject.addProperty("message", "Empty First Name!");
                responseObject.addProperty("errorCode", "EMP-DATA");

            } else if (!v.charLen(firstName, 45)) {
                responseObject.addProperty("message", "First Name Exeed 45 Character Limit!");
                responseObject.addProperty("errorCode", "CHARLEN-EXEED");

            } else if (v.isEmpty(lastName)) {
                responseObject.addProperty("message", "Empty Last Name!");
                responseObject.addProperty("errorCode", "EMP-DATA");

            } else if (!v.charLen(lastName, 45)) {
                responseObject.addProperty("message", "Last Name Exeed 45 Character Limit!");
                responseObject.addProperty("errorCode", "CHARLEN-EXEED");

            } else if (v.isEmpty(mobile)) {
                responseObject.addProperty("message", "Empty Mobile Number!");
                responseObject.addProperty("errorCode", "EMP-DATA");
            } else if (v.validateMobile(mobile)) {
                responseObject.addProperty("message", "Invalid Mobile");
                responseObject.addProperty("errorCode", "INV-DATA");

                //address verification
            } else if (v.isEmpty(line1)) {
                responseObject.addProperty("message", "Empty Address Line 1!");
                responseObject.addProperty("errorCode", "EMP-DATA");

            } else if (!v.charLen(line1, 45)) {
                responseObject.addProperty("message", "Address Line 1 Exeed 45 Character Limit!");
                responseObject.addProperty("errorCode", "CHARLEN-EXEED");

            } else if (v.isEmpty(line2)) {
                responseObject.addProperty("message", "Empty Address Line 2!");
                responseObject.addProperty("errorCode", "EMP-DATA");

            } else if (!v.charLen(line2, 45)) {
                responseObject.addProperty("message", "Address Line 2 Exeed 45 Character Limit!");
                responseObject.addProperty("errorCode", "CHARLEN-EXEED");

            } else if (city.isEmpty()) {
                responseObject.addProperty("message", "Please Select a City!");
                responseObject.addProperty("errorCode", "EMP-DATA");

            } else if (Integer.parseInt(city) <= 0) {
                responseObject.addProperty("message", "Invalid City ID");
                responseObject.addProperty("errorCode", "INV-DATA");
            } else {

                System.out.println("user data ok");

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                int userID = user.getId();

                Criteria userCrt = s.createCriteria(UserData.class);
                Criterion ucrt = Restrictions.eq("userID", userID);
                userCrt.add(ucrt);

                City cityObj = (City) s.load(City.class, cityId);

                boolean updateProfile = false;

                UserData userData = new UserData();
                Address address = new Address();

                address.setId(userID);
                System.out.println("set default user id for address");
                userData.setUserID(userID);
                System.out.println("set default user id for userData");

                System.out.println("checking for existing userData and address");
                if (!userCrt.list().isEmpty()) {
                    updateProfile = true;
                    //get userData if exists
                    System.out.println("Found userData");
                    userData = (UserData) userCrt.uniqueResult();

                    Criteria adrCrt = s.createCriteria(Address.class);
                    Criterion adcrt = Restrictions.eq("id", userID);
                    adrCrt.add(adcrt);

                    //get address if exists
                    if (!adrCrt.list().isEmpty()) {
                        System.out.println("Found address");
                        address = (Address) adrCrt.uniqueResult();
                    }
                }

                // bio update not implimented. hardcoded bio generation
                String bio = "Hello I'm " + firstName + " " + lastName + " From " + cityObj.getName() + ".";

                System.out.println("trying to update userData");

                if (updateProfile) {
                    //update data   
                    address.setLine1(line1);
                    address.setLine2(line2);
                    address.setPostalCode(postalCode);
                    address.setCity(cityObj);

                    s.update(address);
                    s.beginTransaction().commit();
                    System.out.println(" Address Updated!");

                    userData.setFirstName(firstName);
                    userData.setLastName(lastName);
                    userData.setMobile(mobile);
                    userData.setAddress(address);
                    userData.setBio(bio);

                    s.update(userData);
                    s.beginTransaction().commit();
                    System.out.println("User Data Updated!");

                } else {
                    //insert new data

                    System.out.println(userID);

                    address.setLine1(line1);
                    address.setLine2(line2);
                    address.setPostalCode(postalCode);
                    address.setCity(cityObj);

                    s.save(address);
                    s.beginTransaction().commit();
                    System.out.println("New Address Added!");

                    userData.setFirstName(firstName);
                    userData.setLastName(lastName);
                    userData.setMobile(mobile);
                    userData.setAddress(address);
                    userData.setBio(bio);

                    s.save(userData);
                    s.beginTransaction().commit();
                    System.out.println("New User Data Added!");

                }
                responseObject.addProperty("status", true);
                System.out.println("user data update completed");
                responseObject.addProperty("message", "User Data Updated Successfully!");

            }
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
