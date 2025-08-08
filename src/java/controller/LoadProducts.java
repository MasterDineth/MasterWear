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

@WebServlet(name = "LoadProducts", urlPatterns = {"/LoadProducts"})
public class LoadProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //get Category
        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categorydList = c1.list();
        //get Category

        //get Size
        Criteria c2 = s.createCriteria(Size.class);
        List<Size> sizeList = c2.list();
        //get Size

        //get Colour
        Criteria c3 = s.createCriteria(Color.class);
        List<Color> colorList = c3.list();
        //get Colour

        //Load-product-data
        Status status = (Status) s.get(Status.class, 1);
        Criteria c6 = s.createCriteria(Product.class);
        c6.addOrder(Order.desc("id"));
        c6.add(Restrictions.eq("status", status));

        c6.setFirstResult(0);
        c6.setMaxResults(c6.list().size());

        List<Product> productList = c6.list();

        //Load-product-data
        Gson gson = new Gson();

        responseObject.add("categorydList", gson.toJsonTree(categorydList));
        responseObject.add("sizeList", gson.toJsonTree(sizeList));
        responseObject.add("colorList", gson.toJsonTree(colorList));
        responseObject.addProperty("allProductCount", productList.size());
        responseObject.add("productList", gson.toJsonTree(productList));

        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }
}
