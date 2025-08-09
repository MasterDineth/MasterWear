package model;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
    "/html/index.html",
    "/html/all-products.html",
    "/html/add-product.html",
    "/html/pages-account-settings-account.html"

})
public class AdminLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession ses = request.getSession();

        if (ses != null && ses.getAttribute("admin") != null) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect("/MasterWear/html/auth-login-admin.html");
        }
    }

    @Override
    public void destroy() {

    }

}
