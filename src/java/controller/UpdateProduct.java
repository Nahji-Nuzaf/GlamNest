package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.ArmStyle;
import hibernate.Brand;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Material;
import hibernate.Product;
import hibernate.Quality;
import hibernate.Status;
import hibernate.Style;
import hibernate.SubCategory;
import hibernate.User;
import hibernate.Warranty;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "UpdateProduct", urlPatterns = {"/UpdateProduct"})
public class UpdateProduct extends HttpServlet {

    private static final int PENDING_STATUS_ID = 1;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("pid");

        boolean isUpdate = pid != null; // You can determine from request or URL param

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String qty = request.getParameter("qty");
        String color = request.getParameter("color");
        String warrantyId = request.getParameter("warrantyId");
        String catId = request.getParameter("catId");
        String subCatId = request.getParameter("subCatId");
        String brandId = request.getParameter("brandId");
        String promaterialId = request.getParameter("promaterialId");
        String prostyleId = request.getParameter("prostyleId");
        String overallshape = request.getParameter("overallshape");
        String armStyleId = request.getParameter("armStyleId");
        String seating = request.getParameter("seating");
        String proQualityId = request.getParameter("proQualityId");
        String assembly = request.getParameter("assembly");

        Part part1 = request.getPart("image1");
        Part part2 = request.getPart("image2");
        Part part3 = request.getPart("image3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        if (!Util.isInteger(pid)) {
            responseObject.addProperty("message", "Invlaid Product ID");
        } else {

            Product pro = (Product) s.get(Product.class, Integer.valueOf(pid));

            if (pro == null) {
                responseObject.addProperty("message", "Product Not Found");
            } else {

                //validation
                if (request.getSession().getAttribute("user") == null) {
                    responseObject.addProperty("message", "Please sign in!");
                } else if (title.isEmpty()) {
                    responseObject.addProperty("message", "Product Title cannot be empty");
                } else if (description.isEmpty()) {
                    responseObject.addProperty("message", "Product Description cannot be empty");
                } else if (Double.parseDouble(price) <= 0) {
                    responseObject.addProperty("message", "Price must be greater than 0");
                } else if (!Util.isInteger(qty)) {
                    responseObject.addProperty("message", "Invalid quantity");
                } else if (Integer.parseInt(qty) <= 0) {
                    responseObject.addProperty("message", "Quantity must be greater than 0");
                } else if (color.isEmpty()) {
                    responseObject.addProperty("message", "Product Color cannot be empty");
                } else if (!isUpdate && (part1 == null || part1.getSubmittedFileName().isEmpty())) {
                    responseObject.addProperty("message", "Product image one is required");
                } else if (!isUpdate && (part2 == null || part2.getSubmittedFileName().isEmpty())) {
                    responseObject.addProperty("message", "Product image two is required");
                } else if (!isUpdate && (part3 == null || part3.getSubmittedFileName().isEmpty())) {
                    responseObject.addProperty("message", "Product image three is required");
                } else if (!Util.isInteger(warrantyId)) {
                    responseObject.addProperty("message", "Invalid Warranty!");
                } else if (Integer.parseInt(warrantyId) == 0) {
                    responseObject.addProperty("message", "Please Select a Warranty");
                } else if (!Util.isInteger(catId)) {
                    responseObject.addProperty("message", "Invalid Category!");
                } else if (Integer.parseInt(catId) == 0) {
                    responseObject.addProperty("message", "Please Select a Category");
                } else if (!Util.isInteger(subCatId)) {
                    responseObject.addProperty("message", "Invalid Sub Category!");
                } else if (Integer.parseInt(subCatId) == 0) {
                    responseObject.addProperty("message", "Please Select a Sub Category");
                } else if (!Util.isInteger(brandId)) {
                    responseObject.addProperty("message", "Invalid Brand!");
                } else if (Integer.parseInt(brandId) == 0) {
                    responseObject.addProperty("message", "Please Select a Brand");
                } else if (!Util.isInteger(promaterialId)) {
                    responseObject.addProperty("message", "Invalid Product Material!");
                } else if (Integer.parseInt(promaterialId) == 0) {
                    responseObject.addProperty("message", "Please Select a Product Material");
                } else if (!Util.isInteger(prostyleId)) {
                    responseObject.addProperty("message", "Invalid Product Style!");
                } else if (Integer.parseInt(prostyleId) == 0) {
                    responseObject.addProperty("message", "Please Select a Product Style");
                } else if (overallshape.isEmpty()) {
                    responseObject.addProperty("message", "Product Overall Shape cannot be empty");
                } else if (!Util.isInteger(armStyleId)) {
                    responseObject.addProperty("message", "Invalid Arm Style!");
                } else if (Integer.parseInt(armStyleId) == 0) {
                    responseObject.addProperty("message", "Please Select a Arm Style");
                } else if (!Util.isInteger(seating)) {
                    responseObject.addProperty("message", "Invalid Seating Capacity");
                } else if (Integer.parseInt(seating) < 0) {
                    responseObject.addProperty("message", "Seating Capacity must be greater than 0");
                } else if (!Util.isInteger(proQualityId)) {
                    responseObject.addProperty("message", "Invalid Product Quality!");
                } else if (Integer.parseInt(proQualityId) == 0) {
                    responseObject.addProperty("message", "Please Select a Product Quality");
                } else if (assembly.isEmpty()) {
                    responseObject.addProperty("message", "Product Assembly cannot be empty");
                } else {

                    Warranty warranty = (Warranty) s.get(Warranty.class, Integer.parseInt(warrantyId));

                    Category Cat = (Category) s.get(Category.class, Integer.valueOf(catId));

                    if (Cat == null) {
                        responseObject.addProperty("message", "Please select a valid Category !");
                    } else {

                        SubCategory subCat = (SubCategory) s.get(SubCategory.class, Integer.valueOf(subCatId));

                        if (subCat == null) {
                            responseObject.addProperty("message", "Please select a valid Sub Category!");
                        } else {

                            if (subCat.getCategory().getId() != Cat.getId()) {
                                responseObject.addProperty("message", "Please select a suitable Sub Category!");
                            } else {

                                Brand brand = (Brand) s.get(Brand.class, Integer.valueOf(brandId));
                                if (brand == null) {
                                    responseObject.addProperty("message", "Please select a valid Brand");
                                } else {
                                    Material material = (Material) s.get(Material.class, Integer.valueOf(promaterialId));
                                    if (material == null) {
                                        responseObject.addProperty("message", "Please select a valid Product Material");
                                    } else {
                                        Style proStyle = (Style) s.get(Style.class, Integer.valueOf(prostyleId));
                                        if (proStyle == null) {
                                            responseObject.addProperty("message", "Please select a valid Product Style");
                                        } else {
                                            ArmStyle armStyle = (ArmStyle) s.get(ArmStyle.class, Integer.valueOf(armStyleId));
                                            if (armStyle == null) {
                                                responseObject.addProperty("message", "Please select a valid Arm Style");
                                            } else {
                                                Quality proQuality = (Quality) s.get(Quality.class, Integer.valueOf(proQualityId));
                                                if (proQuality == null) {
                                                    responseObject.addProperty("message", "Please select a valid Product Quality");
                                                } else {

                                                    pro.setTitle(title);
                                                    pro.setDescription(description);
                                                    pro.setPrice(Double.parseDouble(price));
                                                    pro.setQty(Integer.parseInt(qty));
//                                                pro.setCreated_at(new Date());
                                                    pro.setSeating_capacity(Integer.parseInt(seating));
                                                    pro.setColor(color);
                                                    pro.setOverall_shape(overallshape);
                                                    pro.setBrand(brand);
                                                    pro.setMaterial(material);

                                                    pro.setWarranty(warranty);
                                                    pro.setStyle(proStyle);
                                                    pro.setArm_style(armStyle);
                                                    pro.setQuality(proQuality);

//                                                User user = (User) request.getSession().getAttribute("user");
//                                                Criteria c1 = s.createCriteria(User.class);
//                                                c1.add(Restrictions.eq("email", user.getEmail()));
//                                                User u1 = (User) c1.uniqueResult();
//                                                pro.setUser(u1);
                                                    pro.setSubcategory(subCat);
                                                    pro.setAssembly(assembly);

                                                    s.update(pro);
                                                    s.beginTransaction().commit();
                                                    s.close();

                                                    // Image upload path setup
                                                    String appPath = getServletContext().getRealPath("");
                                                    String newPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "product-images");
                                                    File productFolder = new File(newPath, String.valueOf(pro.getId()));

                                                    if (!productFolder.exists()) {
                                                        productFolder.mkdir();
                                                    }

                                                    // Replace images only if new files uploaded
                                                    if (part1 != null && !part1.getSubmittedFileName().isEmpty()) {
                                                        File file1 = new File(productFolder, "image1.png");
                                                        Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                                    }
                                                    if (part2 != null && !part2.getSubmittedFileName().isEmpty()) {
                                                        File file2 = new File(productFolder, "image2.png");
                                                        Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                                    }
                                                    if (part3 != null && !part3.getSubmittedFileName().isEmpty()) {
                                                        File file3 = new File(productFolder, "image3.png");
                                                        Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                                    }

                                                    responseObject.addProperty("status", true);
                                                    responseObject.addProperty("message", "Product updated successfully.");
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }

                    }

                }

            }

        }

        //send response
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        //send response

    }

}
