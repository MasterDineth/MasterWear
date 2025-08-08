package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet(name = "LoadProductTitles", urlPatterns = {"/LoadProductTitles"})
public class LoadProductTitles extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("status", false);

        Criteria c1 = s.createCriteria(Product.class);
        List<City> productList = c1.list();


        responseObject.addProperty("status", true);

        responseObject.add("productList", gson.toJsonTree(productList));
        
        System.out.println("product title data loaded and sent");

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }
}
