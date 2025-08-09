package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Size;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Category;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;

@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Session s = HibernateUtil.getSessionFactory().openSession();

        //get Brand
        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categoryList = c1.list();
        //get Brand

        //get Model
        Criteria c2 = s.createCriteria(Size.class);
        List<Size> sizeList = c2.list();
        //get Model

        //get Colour
        Criteria c3 = s.createCriteria(Color.class);
        List<Color> colorList = c3.list();
        //get Colour

        s.close();

        Gson gson = new Gson();

        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("sizeList", gson.toJsonTree(sizeList));
        responseObject.add("colorList", gson.toJsonTree(colorList));


        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
