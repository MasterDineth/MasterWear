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

@WebServlet(name = "SearchProductsByFilter", urlPatterns = {"/SearchProductsByFilter"})
public class SearchProductsByFilter extends HttpServlet {

    private static final int MAX_RESULT = 6;
    private static final int ACTIVE_ID = 1;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        System.out.println("ok");
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        System.out.println("SearchProductsByFilter payload: "
                + requestJsonObject.toString());

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Product.class);

        if (!requestJsonObject.get("categoryName").getAsString().isEmpty()) {

            String categoryValue = requestJsonObject.get("categoryName").getAsString();
            //get category details
            Criteria c2 = s.createCriteria(Category.class);
            c2.add(Restrictions.eq("name", categoryValue));
            Category category = (Category) c2.uniqueResult();

            //filter product by using category
            c1.add(Restrictions.eq("category", category));
            
            System.out.println("filtered by category");

        }

        if (!requestJsonObject.get("sizeName").getAsString().isEmpty()) {

            String sizeValue = requestJsonObject.get("sizeName").getAsString();
            //get Size details
            Criteria c3 = s.createCriteria(Size.class);
            c3.add(Restrictions.eq("name", sizeValue));
            Size size = (Size) c3.uniqueResult();

            //filter product by using Size
            c1.add(Restrictions.eq("size", size));
             System.out.println("filtered by size");

        }

        if (!requestJsonObject.get("colorName").getAsString().isEmpty()) {

            String colorName = requestJsonObject.get("colorName").getAsString();
            //get color details
            Criteria c4 = s.createCriteria(Color.class);
            c4.add(Restrictions.eq("name", colorName));
            Color color = (Color) c4.uniqueResult();

            //filter product by using color
            c1.add(Restrictions.eq("color", color));
             System.out.println("filtered by color");

            //get color details
        }

        if (requestJsonObject.has("priceStart") & requestJsonObject.has("priceEnd")) {
            double priceStart = requestJsonObject.get("priceStart").getAsDouble();
            double priceEnd = requestJsonObject.get("priceEnd").getAsDouble();

            c1.add(Restrictions.ge("price", priceStart));
            c1.add(Restrictions.le("price", priceEnd));
            
             System.out.println("filtered by price");
        }

        if (requestJsonObject.has("sortValue")) {
            String sortValue = requestJsonObject.get("sortValue").getAsString();
            if (sortValue.equals("Sort by Latest")) {
                c1.addOrder(Order.desc("id"));
            } else if (sortValue.equals("Sort by Oldest")) {
                c1.addOrder(Order.asc("id"));

            } else if (sortValue.equals("Sort by Name")) {
                c1.addOrder(Order.asc("title"));
            } else if (sortValue.equals("Sort by Price")) {
                c1.addOrder(Order.asc("price"));
            }
            
             System.out.println("filtered by sort order");
        }

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SearchProductsByFilter.MAX_RESULT);
             System.out.println("max result set");
        }

        //get filter product list
        Status status = (Status) s.get(Status.class, ACTIVE_ID); // get Active product 1 = Active
        c1.add(Restrictions.eq("status", status));
        List<Product> productList = c1.list();

         System.out.println("all filters applied");
        //hibernate session close
        s.close();

        responseObject.add("productList", gson.toJsonTree(productList));
        responseObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        System.out.println(toJson);
        response.getWriter().write(toJson);

    }

}
