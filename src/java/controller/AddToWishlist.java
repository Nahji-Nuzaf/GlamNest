package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
import hibernate.Wishlist;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HP
 */
@WebServlet(name = "AddToWishlist", urlPatterns = {"/AddToWishlist"})
public class AddToWishlist extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("pid");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!Util.isInteger(productId)) {
            responseObject.addProperty("message", "Invalid Product ID");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            Product product = (Product) s.load(Product.class, Integer.parseInt(productId));

            if (product == null) {
                responseObject.addProperty("message", "Product not Found");
            } else {

                User user = (User) request.getSession().getAttribute("user");

                if (user != null) { //add to DB Wishlist

                    Criteria c1 = s.createCriteria(Wishlist.class);
                    c1.add(Restrictions.eq("user", user));
                    c1.add(Restrictions.eq("product", product));

                    if (c1.list().isEmpty()) { //can add the product to wishlist

                        Wishlist wishlist = new Wishlist();

                        wishlist.setProduct(product);
                        wishlist.setUser(user);

                        s.save(wishlist);
                        tr.commit();
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Product added to the wishlist successfully");

                    } else {
                        responseObject.addProperty("message", "Product already in the wishlist");
                    }

                } else { //add to session cart

                    HttpSession ses = request.getSession();

                    if (ses.getAttribute("sessionWishlist") == null) { //checks whether the session for wishlist exists

                        ArrayList<Wishlist> sessionWish = new ArrayList<>();

                        Wishlist wish = new Wishlist();
                        wish.setUser(null);
                        wish.setProduct(product);

                        sessionWish.add(wish);

                        ses.setAttribute("sessionWishlist", sessionWish);
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Product added to the Wishlist !!");

                    } else { //If the session for wishlist exists

                        ArrayList<Wishlist> sessionWList = (ArrayList<Wishlist>) ses.getAttribute("sessionWishlist");
                        Wishlist foundWishProduct = null;

                        for (Wishlist wishlist : sessionWList) {
                            if (wishlist.getProduct().getId() == product.getId()) {
                                foundWishProduct = wishlist;
                                break;
                            }
                        }

                        if (foundWishProduct != null) {

                            responseObject.addProperty("message", "Product already in the wishlist");

                        } else {

                            foundWishProduct = new Wishlist();

                            foundWishProduct.setProduct(product);
                            foundWishProduct.setUser(null);

                            sessionWList.add(foundWishProduct);
                            responseObject.addProperty("status", true);
                            responseObject.addProperty("message", "Product added to the wishlist successfully");
                        }

                    }

                }

            }

        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
