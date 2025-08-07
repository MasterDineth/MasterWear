package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import service.CartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.CartLoadService;

@WebServlet(name = "LoadCartProducts", urlPatterns = {"/LoadCartProducts"})
public class LoadCartProducts extends HttpServlet {

    private final CartService cartService = new CartService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CartLoadService cls = new CartLoadService();
        JsonObject responseObject = cls.getCartResponse(request);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }
}