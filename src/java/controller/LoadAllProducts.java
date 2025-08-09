package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Size;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Category;
import hibernate.Product;
import hibernate.Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadAllProducts", urlPatterns = {"/LoadAllProducts"})
public class LoadAllProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Product.class);
        c1.addOrder(Order.asc("id"));

        List<Product> productList = c1.list();

        //Load-product-data
        Gson gson = new Gson();        
        responseObject.addProperty("allProductCount", productList.size());
        responseObject.add("product", gson.toJsonTree(productList));

        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }
}
