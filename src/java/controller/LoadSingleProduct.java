package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.SubCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String productId = request.getParameter("id");

        if (Util.isInteger(productId)) {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            try {
                Product product = (Product) s.get(Product.class, Integer.valueOf(productId));

                if (product.getStatus().getValue().equals("Active")) {

                    product.getUser().setEmail(null);
                    product.getUser().setPassword(null);
                    product.getUser().setVerification(null);
                    product.getUser().setCreated_at(null);
                    product.getUser().setId(-1);

                    // load similar products
                    Criteria c1 = s.createCriteria(SubCategory.class);
                    c1.add(Restrictions.eq("category", product.getSubcategory().getCategory()));
                    List<SubCategory> subcategoryList = c1.list();
//
                    Criteria c2 = s.createCriteria(Product.class);
                    c2.add(Restrictions.in("subcategory", subcategoryList));
                    c2.add(Restrictions.ne("id", product.getId()));
                    c2.setMaxResults(4);
                    List<Product> productList = c2.list();

                    for (Product pr : productList) {
                        pr.getUser().setEmail(null);
                        pr.getUser().setPassword(null);
                        pr.getUser().setVerification(null);
                        pr.getUser().setCreated_at(null);
                        pr.getUser().setId(-1);
                    }

                    // load similar products
//                System.out.println(product);
                    responseObject.add("product", gson.toJsonTree(product));
                    responseObject.add("productList", gson.toJsonTree(productList));
                    responseObject.addProperty("status", true);

                } else {
                    responseObject.addProperty("message", "Product Not Active Yet");
                    System.out.println("Product Not Active Yet");
                }

            } catch (Exception e) {
//                responseObject.addProperty("message", "Product Not Found");
//                System.out.println("Product Not Found");
                e.printStackTrace();
            }

        }

        response.setContentType("application/json");
        String tojson = gson.toJson(responseObject);
        response.getWriter().write(tojson);

//        System.out.println(productId);
    }

}
