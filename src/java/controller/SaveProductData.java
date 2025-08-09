package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Size;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Category;
import hibernate.Product;

import hibernate.Status;

import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "SaveProductData", urlPatterns = {"/SaveProductData"})
public class SaveProductData extends HttpServlet {

    private final static int PENTING_STATUS_ID = 2;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("OK 1");
        String categoryId = request.getParameter("categoryId");
        String sizeId = request.getParameter("sizeId");
        String colorId = request.getParameter("colorId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String qty = request.getParameter("qty");

        System.out.println(price);
        System.out.println(qty);

        Part part1 = request.getPart("image");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //validation
   
        if (request.getSession().getAttribute("admin") == null) {
            responseObject.addProperty("message", "please sign in!");
        } else if (!Validations.isInteger(sizeId)) {
            responseObject.addProperty("message", "Invalid model!");
        } else if (Integer.parseInt(sizeId) == 0) {
            responseObject.addProperty("message", "Please select a model!");
        } else if (!Validations.isInteger(categoryId)) {
            responseObject.addProperty("message", "Invalid brand!");
        } else if (Integer.parseInt(categoryId) == 0) {
            responseObject.addProperty("message", "Please select a brand!");
        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Product Title can not be empty!");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Product description can not be empty!");
        } else if (!Validations.isInteger(colorId)) {
            responseObject.addProperty("message", "Invalid color!");
        } else if (Integer.parseInt(colorId) == 0) {
            responseObject.addProperty("message", "Please select a valid color!");
        } else if (!Validations.isDouble(price)) {
            responseObject.addProperty("message", "Invalid price!");
        } else if (Double.parseDouble(price) <= 0) {
            responseObject.addProperty("message", "Price must be greater than 0!");
        } else if (!Validations.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid Quantity!");
        } else if (Integer.parseInt(qty) <= 0) {
            responseObject.addProperty("message", "Quantity must be greater than 0!");
        } else if (part1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Select An Image!");
        } else {

            Category category = (Category) s.get(Category.class, Integer.valueOf(categoryId));
            if (category == null) {
                responseObject.addProperty("message", "Please select a valid Category Name");
            } else {

                Size size = (Size) s.get(Size.class, Integer.parseInt(sizeId));

                if (size == null) {
                    responseObject.addProperty("message", "Please select a Size!");
                } else {

                    Color color = (Color) s.get(Color.class, Integer.valueOf(colorId));
                    if (color == null) {
                        responseObject.addProperty("message", "Please select a valid color");
                    } else {

                        Product p = new Product();
                        p.setSize(size);
                        p.setCategory(category);
                        p.setTitle(title);
                        p.setDescription(description);
                        p.setColor(color);
                        p.setPrice(Double.parseDouble(price));
                        p.setQty(Integer.parseInt(qty));

                        Status status = (Status) s.load(Status.class, PENTING_STATUS_ID);
                        p.setStatus(status);
                        p.setDateAdded(new Date());

                        int id = (int) s.save(p);
                        s.beginTransaction().commit();
                        s.close();

                        System.out.println(id);

                        //image-uploading
//                        String appPath = getServletContext().getRealPath("");                        
//                        System.out.println(appPath);
//
//                        String newPath = appPath.replace("build//web//images", "web//images//product");                        
//                        System.out.println(newPath);
//                        File productFolder = new File(newPath, String.valueOf(id));
                        String basePath = "C:\\Users\\MasterDineth\\OneDrive - MSFT\\Documents\\Projects\\New Project\\MasterWear\\web\\images\\product";
                        File productFolder = new File(basePath, String.valueOf(id));

                        productFolder.mkdir();

                        File file1 = new File(productFolder, "image1.jpg");
                        Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        //image-uploading

                        responseObject.addProperty("status", true);
                       responseObject.addProperty("message", "Product Registered Successfully!");
                        System.out.println("product registered!");
                    }
                }
            }
        }

        //validation
        //send response
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        //send response
    }
}
