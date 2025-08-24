package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.OrderItems;
import hibernate.OrderStatus;
import hibernate.Orders;
import hibernate.Product;
import hibernate.User;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HP
 */
@WebServlet(name = "LoadPurchaseHistory", urlPatterns = {"/LoadPurchaseHistory"})
public class LoadPurchaseHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) { //process if user logged in

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c1 = s.createCriteria(Orders.class);
            c1.add(Restrictions.eq("user", user));
            List<Orders> ordersList = c1.list();
            
            if(ordersList.isEmpty()){
                responseObject.addProperty("message", "No Products Purchased Yet");
            }else{
                
                Criteria c2 = s.createCriteria(OrderItems.class);
                c2.add(Restrictions.in("orders", ordersList));
                List<OrderItems> orderItemsList = c2.list();
                
                responseObject.add("orderItemsList", gson.toJsonTree(orderItemsList));
                
            }
            
            responseObject.add("ordersList", gson.toJsonTree(ordersList));

        }

        responseObject.addProperty("status", true);
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

}
