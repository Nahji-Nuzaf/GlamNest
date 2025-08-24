package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.Wishlist;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadWishlistItems", urlPatterns = {"/LoadWishlistItems"})
public class LoadWishlistItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");
        
        if(user != null){ //from DB
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c1 = s.createCriteria(Wishlist.class);
            c1.add(Restrictions.eq("user", user));
            List<Wishlist> wishList = c1.list();
            
            if (wishList.isEmpty()) {
                responseObject.addProperty("message", "Your Wishlist is empty...");
            } else {
                for (Wishlist wish : wishList) {
                    wish.getProduct().setUser(null);
                    wish.setUser(null);
                }
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Wishlist items successfully loded");
                responseObject.add("wishlistItems", gson.toJsonTree(wishList));
            }
            
        }else{ //from session
            
            ArrayList<Wishlist> sessionWishlist = (ArrayList<Wishlist>) request.getSession().getAttribute("sessionWishlist");
            if (sessionWishlist != null) {
                if (sessionWishlist.isEmpty()) {
                    responseObject.addProperty("message", "Your Wishlist is empty...");
                } else {
                    for (Wishlist sessionwishlist : sessionWishlist) {
                        sessionwishlist.getProduct().setUser(null);
                        sessionwishlist.setUser(null);
                    }
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Wishlist items successfully loded");
                    responseObject.add("wishlistItems", gson.toJsonTree(sessionWishlist));
                }
            } else {
                responseObject.addProperty("message", "Your Wishlist is empty...");
            }
            
        }
        
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
