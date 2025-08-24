package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Product;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadUpdateProduct", urlPatterns = {"/LoadUpdateProduct"})
public class LoadUpdateProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String proId = request.getParameter("id");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if(!Util.isInteger(proId)){
            responseObject.addProperty("message", "Invlaid Product ID");
        }else{
            
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Product product = (Product) s.get(Product.class, Integer.valueOf(proId));
            
            responseObject.add("product", gson.toJsonTree(product));
            responseObject.addProperty("status", true);
            
        }
        
        response.setContentType("application/json");
        String tojson = gson.toJson(responseObject);
        response.getWriter().write(tojson);
        
    }

}
