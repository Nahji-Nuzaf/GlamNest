package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.OrderItems;
import hibernate.Orders;
import hibernate.Product;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@WebServlet(name = "LoadSellerDashboard", urlPatterns = {"/LoadSellerDashboard"})
public class LoadSellerDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) { //process if user logged in

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(Product.class);
            c1.add(Restrictions.eq("user", user));
            List<Product> productList = c1.list();
            
            List<Orders> ordersList = new ArrayList<>();

            if (!productList.isEmpty()) {

                Criteria c2 = s.createCriteria(OrderItems.class);
                c2.add(Restrictions.in("product", productList));
                List<OrderItems> orderItemsList = c2.list();

                // Step 3: Get orders linked to those order items
                if (!orderItemsList.isEmpty()) {
                    Set<Orders> ordersSet = new HashSet<>(); // to avoid duplicates
                    for (OrderItems oi : orderItemsList) {
                        ordersSet.add(oi.getOrders()); // assuming getOrders() is the relationship method
                    }
                    ordersList = new ArrayList<>(ordersSet);
                }

                responseObject.add("orderItemsList", gson.toJsonTree(orderItemsList));
                responseObject.add("ordersList", gson.toJsonTree(ordersList));

            }
            
            Criteria c3 = s.createCriteria(Address.class);
            List<Address> addressList = c3.list();

            responseObject.add("addressList", gson.toJsonTree(addressList));
            responseObject.add("productList", gson.toJsonTree(productList));

        } else {

        }

        responseObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
