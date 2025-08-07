package model;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/error-handler")
public class ErrorHandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode != null) {
            if (statusCode == 404) {
                request.getRequestDispatcher("/page-404.html").forward(request, response);
            } else if (statusCode == 500) {
                request.getRequestDispatcher("/page-500.html").forward(request, response);
            } else {
                response.getWriter().write("<h1>Unexpected Error</h1>");
            }
        }
    }
}
