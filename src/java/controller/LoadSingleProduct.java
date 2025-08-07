package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        String productId = request.getParameter("id");

        if (Validations.isInteger(productId)) {
            Session s = HibernateUtil.getSessionFactory().openSession();
            try {

                System.out.println("trying to serach product");

                Product product = (Product) s.get(Product.class, Integer.valueOf(productId));

                if (product.getStatus().getName().equals("Active")) {

                    Criteria c1 = s.createCriteria(Category.class);

                    c1.add(Restrictions.eq("name", product.getCategory().getName()));
                    List<Category> categoryList = c1.list();

//                    Criteria c2 = s.createCriteria(Product.class);
//                    c2.add(Restrictions.in("category", categoryList));
//                    c2.add(Restrictions.ne("id", product.getId()));
//                    c2.setMaxResults(6);
//                    List<Product> productList = c2.list();  
                    //similer-product-data
                    responseObject.add("product", gson.toJsonTree(product));
//                    responseObject.add("productList", gson.toJsonTree(productList));
                    responseObject.addProperty("status", true);

                } else {

                    System.out.println("product not founda");
                    responseObject.addProperty("message", "Product not found");

                }
            } catch (Exception e) {
                e.printStackTrace();
                responseObject.addProperty("message", "Product not found");
            }
        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
