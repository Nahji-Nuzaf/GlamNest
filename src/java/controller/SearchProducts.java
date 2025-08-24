package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Material;
import hibernate.Product;
import hibernate.Quality;
import hibernate.Status;
import hibernate.Style;
import hibernate.SubCategory;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HP
 */
@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    private static final int MAX_RESULT = 2;
    private static final int ACTIVE_ID = 1;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Product.class); // get all products for filtering

        if(requestJsonObject.has("basicSearch")){
            String textSearch = requestJsonObject.get("basicSearch").getAsString();
            
            c1.add(Restrictions.like("title", textSearch, MatchMode.ANYWHERE));
        }
        
        if (requestJsonObject.has("categoryName")) {
            String categoryName = requestJsonObject.get("categoryName").getAsString();

            // get category details 
            Criteria c2 = s.createCriteria(Category.class);
            c2.add(Restrictions.eq("name", categoryName));
            Category category = (Category) c2.uniqueResult();

            // filter Sub Category by using category details
            Criteria c3 = s.createCriteria(SubCategory.class);
            c3.add(Restrictions.eq("category", category));
            List<SubCategory> subCategoryList = c3.list();

            // filter product using subCategoryList
            c1.add(Restrictions.in("subcategory", subCategoryList));

        }
        
        if (requestJsonObject.has("brandName")) {
            String brandNamee = requestJsonObject.get("brandName").getAsString();

            // get qulity details
            Criteria c4 = s.createCriteria(Brand.class);
            c4.add(Restrictions.eq("name", brandNamee));
            Brand brand = (Brand) c4.uniqueResult();

            // filter product by using quality
            c1.add(Restrictions.eq("brand", brand));
        }
        
        if (requestJsonObject.has("materialName")) {
            String materialValue = requestJsonObject.get("materialName").getAsString();

            // get qulity details
            Criteria c5 = s.createCriteria(Material.class);
            c5.add(Restrictions.eq("value", materialValue));
            Material material = (Material) c5.uniqueResult();

            // filter product by using quality
            c1.add(Restrictions.eq("material", material));
        }
        
        if (requestJsonObject.has("conditionName")) {
            String qualityName = requestJsonObject.get("conditionName").getAsString();

            // get qulity details
            Criteria c6 = s.createCriteria(Quality.class);
            c6.add(Restrictions.eq("value", qualityName));
            Quality quality = (Quality) c6.uniqueResult();

            // filter product by using quality
            c1.add(Restrictions.eq("quality", quality));
        }
        
        if (requestJsonObject.has("styleName")) {
            String styleValue = requestJsonObject.get("styleName").getAsString();

            // get qulity details
            Criteria c4 = s.createCriteria(Style.class);
            c4.add(Restrictions.eq("value", styleValue));
            Style style = (Style) c4.uniqueResult();

            // filter product by using quality
            c1.add(Restrictions.eq("style", style));
        }
        
        if (requestJsonObject.has("minPrice") && requestJsonObject.has("maxPrice")) {
            double priceStart = requestJsonObject.get("minPrice").getAsDouble();
            double priceEnd = requestJsonObject.get("maxPrice").getAsDouble();

            c1.add(Restrictions.ge("price", priceStart));
            c1.add(Restrictions.le("price", priceEnd));
        }
        
        Status status = (Status) s.get(Status.class, SearchProducts.ACTIVE_ID); // get Active product [2 = Active]
        c1.add(Restrictions.eq("status", status));

        resposeObject.addProperty("allProductCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SearchProducts.MAX_RESULT);
        }

        // get filtered product list
        List<Product> productList = c1.list();
        for (Product product : productList) {
            product.setUser(null);
        }
// hibernate session close
        s.close();

        resposeObject.add("productList", gson.toJsonTree(productList));
        resposeObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(resposeObject);
        response.getWriter().write(toJson);
    }

}
