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
import hibernate.Warranty;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Gson gson = new Gson();

        //search category
        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categoryList = c1.list();
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        //search category

        //search subcategory
        Criteria c2 = s.createCriteria(SubCategory.class);
        List<SubCategory> subcategoryList = c2.list();
        responseObject.add("subcategoryList", gson.toJsonTree(subcategoryList));
        //search subcategory

        //search brand
        Criteria c3 = s.createCriteria(Brand.class);
        List<Brand> brandList = c3.list();
        responseObject.add("brandList", gson.toJsonTree(brandList));
        //search brand

        //search product Material
        Criteria c4 = s.createCriteria(Material.class);
        List<Material> materialList = c4.list();
        responseObject.add("materialList", gson.toJsonTree(materialList));
        //search product Material

        //search product style
        Criteria c5 = s.createCriteria(Style.class);
        List<Style> styleList = c5.list();
        responseObject.add("styleList", gson.toJsonTree(styleList));
        //search product style

        //search arm style
        Criteria c6 = s.createCriteria(ArmStyle.class);
        List<ArmStyle> armstyleList = c6.list();
        responseObject.add("armstyleList", gson.toJsonTree(armstyleList));
        //search arm style

        //search warranty
        Criteria c7 = s.createCriteria(Warranty.class);
        List<Warranty> warrantyList = c7.list();
        responseObject.add("warrantyList", gson.toJsonTree(warrantyList));
        //search warranty

        //search quality
        Criteria c8 = s.createCriteria(Quality.class);
        List<Quality> qualityList = c8.list();
        responseObject.add("qualityList", gson.toJsonTree(qualityList));
        //search quality

        //load Product data
        Status status = (Status) s.get(Status.class, 1);
        Criteria c9 = s.createCriteria(Product.class);
        c9.addOrder(Order.desc("id"));
        c9.add(Restrictions.eq("status", status));
        
        responseObject.addProperty("allProductCount", c9.list().size());
        
        c9.setFirstResult(0);
        c9.setMaxResults(6);
        
        List<Product> productList = c9.list();
        for (Product product : productList) {
            product.setUser(null);
        }
        
        responseObject.add("productList", gson.toJsonTree(productList));
        //load Product data
        
        
        responseObject.addProperty("status", true);
        System.out.println(gson.toJson(responseObject));

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        s.close();

    }

}
