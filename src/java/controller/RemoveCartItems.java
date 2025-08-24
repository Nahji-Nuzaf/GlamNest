package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.User;
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
@WebServlet(name = "RemoveCartItems", urlPatterns = {"/RemoveCartItems"})
public class RemoveCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("pid");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!Util.isInteger(pid)) {
            responseObject.addProperty("message", "Invalid product Id!");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            Product product = (Product) s.get(Product.class, Integer.valueOf(pid));

            if (product == null) {
                responseObject.addProperty("message", "Product not found");
            } else {

                User user = (User) request.getSession().getAttribute("user");

                if (user != null) {

                    // ===== DB-based cart removal =====
                    Criteria c1 = s.createCriteria(Cart.class);
                    c1.add(Restrictions.eq("user", user));
                    c1.add(Restrictions.eq("product", product));

                    Cart cart = (Cart) c1.uniqueResult();

                    if (cart != null) {
                        s.delete(cart);
                        tr.commit();
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Product removed from cart successfully");
                    } else {
                        responseObject.addProperty("message", "Product not found in cart");
                    }

                } else {
                    // ===== Session-based cart removal =====
                    HttpSession session = request.getSession();
                    ArrayList<Cart> sessionCart = (ArrayList<Cart>) session.getAttribute("sessionCart");

                    if (sessionCart != null) {
                        Cart cartToRemove = null;
                        for (Cart c : sessionCart) {
                            if (c.getProduct().getId() == product.getId()) {
                                cartToRemove = c;
                                break;
                            }
                        }

                        if (cartToRemove != null) {
                            sessionCart.remove(cartToRemove);
                            responseObject.addProperty("status", true);
                            responseObject.addProperty("message", "Product removed from session cart successfully");
                        } else {
                            responseObject.addProperty("message", "Product not found in session cart");
                        }
                    } else {
                        responseObject.addProperty("message", "Cart is empty");
                    }
                }

            }
            s.close(); // always close the session
        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
