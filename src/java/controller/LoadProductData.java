package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.ArmStyle;
import hibernate.Brand;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Material;
import hibernate.Quality;
import hibernate.Style;
import hibernate.SubCategory;
import hibernate.Warranty;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
//        System.out.println("Ok Got it");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        //search category
        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categoryList = c1.list();
        //search category
        
        //search subcategory
        Criteria c2 = s.createCriteria(SubCategory.class);
        List<SubCategory> subcategoryList = c2.list();
        //search subcategory
        
        //search brand
        Criteria c3 = s.createCriteria(Brand.class);
        List<Brand> brandList = c3.list();
        //search brand
        
        //search product Material
        Criteria c4 = s.createCriteria(Material.class);
        List<Material> materialList = c4.list();
        //search product Material
        
        //search product style
        Criteria c5 = s.createCriteria(Style.class);
        List<Style> styleList = c5.list();
        //search product style
        
        //search arm style
        Criteria c6 = s.createCriteria(ArmStyle.class);
        List<ArmStyle> armstyleList = c6.list();
        //search arm style
        
        //search quality
        Criteria c8 = s.createCriteria(Quality.class);
        List<Quality> qualityList = c8.list();
        //search quality
        
        //search warranty
        Criteria c7 = s.createCriteria(Warranty.class);
        List<Warranty> warrantyList = c7.list();
        //search warranty
        
        
        Gson gson = new Gson();
        
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("subcategoryList", gson.toJsonTree(subcategoryList));
        responseObject.add("brandList", gson.toJsonTree(brandList));
        responseObject.add("materialList", gson.toJsonTree(materialList));
        responseObject.add("styleList", gson.toJsonTree(styleList));
        responseObject.add("armstyleList", gson.toJsonTree(armstyleList));
        responseObject.add("qualityList", gson.toJsonTree(qualityList));
        responseObject.add("warrantyList", gson.toJsonTree(warrantyList));

        responseObject.addProperty("status", true);
        
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        s.close();
        
    }

    
    
}
