package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.City;

import hibernate.HibernateUtil;
import hibernate.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SearchProcess", urlPatterns = {"/SearchProcess"})
public class SearchProcess extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        responseObject.addProperty("status", false);

        Criteria c1 = s.createCriteria(Product.class);
        
        String searchText = requestJsonObject.get("searchText").getAsString();
        
        if (!requestJsonObject.get("searchText").getAsString().isEmpty()) {            
            c1.add(Restrictions.like("title", searchText, MatchMode.ANYWHERE));
            System.out.println("Searched by Title");
        }

        responseObject.addProperty("status", true);
        List<Product> productList = c1.list();

        responseObject.add("productList", gson.toJsonTree(productList));
        System.out.println("product title data loaded and sent");

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }
}
