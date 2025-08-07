package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.User;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class CartLoadService {

    private final Gson gson = new Gson();

    public JsonObject getCartResponse(HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        List<Cart> cartItems;

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            cartItems = loadCartFromDatabase(user);
        } else {
            cartItems = loadCartFromSession(request);
        }

        if (cartItems == null || cartItems.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Your cart is empty.");
        } else {
            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Cart items successfully loaded.");
            responseObject.add("cartItems", gson.toJsonTree(cartItems));
        }

        return responseObject;
    }

    private List<Cart> loadCartFromDatabase(User user) {
        SessionFactory sf = HibernateUtil.getSessionFactory();

        Session session = sf.openSession();
        Criteria criteria = session.createCriteria(Cart.class);
        criteria.add(Restrictions.eq("user", user));
        return criteria.list();

    }

    @SuppressWarnings("unchecked")
    private List<Cart> loadCartFromSession(HttpServletRequest request) {
        return (ArrayList<Cart>) request.getSession().getAttribute("sessionCart");
    }
}
