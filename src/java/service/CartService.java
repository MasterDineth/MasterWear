package service;

import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class CartService {

    public JsonObject addToCart(String prId, String qty, User user, HttpSession session) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!model.Validations.isInteger(prId)) {
            responseObject.addProperty("message", "Invalid product id");
            return responseObject;
        }

        if (!model.Validations.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid Product Quantity");
            return responseObject;
        }

        int productId = Integer.parseInt(prId);
        int quantity = Integer.parseInt(qty);

        Session hSession = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = hSession.beginTransaction();
        Product product = (Product) hSession.get(Product.class, productId);

        if (product == null) {
            responseObject.addProperty("message", "Product not found");
            return responseObject;
        }

        if (user != null) {
            handleUserCart(hSession, tr, user, product, quantity, responseObject);
        } else {
            handleSessionCart(session, product, quantity, responseObject);
        }

        hSession.close();
        return responseObject;
    }

    private void handleUserCart(Session hSession, Transaction tr, User user, Product product, int quantity, JsonObject responseObject) {
        Criteria criteria = hSession.createCriteria(Cart.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("product", product));

        Cart cart = (Cart) criteria.uniqueResult();

        if (cart == null) {
            if (quantity <= product.getQty()) {
                cart = new Cart();
                cart.setQty(quantity);
                cart.setUser(user);
                cart.setProduct(product);
                hSession.save(cart);
                tr.commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Product added to Database cart successfully");
            } else {
                responseObject.addProperty("message", "OOPS... Insufficient product quantity!!!");
            }
        } else {
            int newQty = cart.getQty() + quantity;
            if (newQty <= product.getQty()) {
                cart.setQty(newQty);
                hSession.update(cart);
                tr.commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Database Product cart successfully updated...");
            } else {
                responseObject.addProperty("message", "OOPS... Insufficient product quantity!!!");
            }
        }
    }

    private void handleSessionCart(HttpSession session, Product product, int quantity, JsonObject responseObject) {
        List<Cart> sessionCart = (List<Cart>) session.getAttribute("sessionCart");

        if (sessionCart == null) {
            if (quantity <= product.getQty()) {
                sessionCart = new ArrayList<>();
                Cart cart = new Cart();
                cart.setQty(quantity);
                cart.setUser(null);
                cart.setProduct(product);
                sessionCart.add(cart);
                session.setAttribute("sessionCart", sessionCart);
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Product added to the Session cart");
            } else {
                responseObject.addProperty("message", "OOPS... Insufficient product quantity!!!");
            }
        } else {
            Cart existingCart = null;
            for (Cart cart : sessionCart) {
                if (cart.getProduct().getId() == product.getId()) {
                    existingCart = cart;
                    break;
                }
            }

            if (existingCart != null) {
                int newQty = existingCart.getQty() + quantity;
                if (newQty <= product.getQty()) {
                    existingCart.setQty(newQty);
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Product Session cart updated");
                } else {
                    responseObject.addProperty("message", "OOPS... Insufficient product quantity!!!");
                }
            } else {
                Cart cart = new Cart();
                cart.setQty(quantity);
                cart.setUser(null);
                cart.setProduct(product);
                sessionCart.add(cart);
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Product added to the cart");
            }
        }
    }
}