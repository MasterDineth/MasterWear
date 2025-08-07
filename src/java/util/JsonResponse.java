package util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import java.util.List;

public class JsonResponse {
    private final JsonObject root = new JsonObject();

    public JsonResponse status(boolean ok) {
        root.addProperty("status", ok);
        return this;
    }

    public JsonResponse message(String text) {
        root.addProperty("message", text);
        return this;
    }

    public JsonResponse data(List<Cart> items) {
        root.add("cartItems", new Gson().toJsonTree(items));
        return this;
    }

    public String toJson() {
        return new Gson().toJson(root);
    }
}