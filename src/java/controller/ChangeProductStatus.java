package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.Status;
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
import org.hibernate.Transaction;

/**
 *
 * @author HP
 */
@WebServlet(name = "ChangeProductStatus", urlPatterns = {"/ChangeProductStatus"})
public class ChangeProductStatus extends HttpServlet {

    private static final int DEACTIVE_STATUS_ID = 3;
    private static final int ACTIVE_STATUS_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("pid");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!Util.isInteger(productId)) {
            responseObject.addProperty("message", "Invalid Product Id");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            Product product = (Product) s.get(Product.class, Integer.valueOf(productId));

            if (product == null) {
                responseObject.addProperty("message", "Product not found");
            } else {

                int currentStatusId = product.getStatus().getId();
                Status newStatus;
                String message;

                if (currentStatusId == ACTIVE_STATUS_ID) {
                    newStatus = (Status) s.get(Status.class, DEACTIVE_STATUS_ID);
                    message = "Product deactivated successfully";
                } else if (currentStatusId == DEACTIVE_STATUS_ID) {
                    newStatus = (Status) s.get(Status.class, ACTIVE_STATUS_ID);
                    message = "Product activated successfully";
                } else {
                    // If status is neither active nor deactive, just return a message
                    responseObject.addProperty("message", "Product has an unknown status, cannot toggle");
                    tr.rollback();
                    s.close();

                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(responseObject));
                    return;
                }
                product.setStatus(newStatus);

                tr.commit(); // Hibernate auto-updates the DB because product is persistent

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", message);

            }
            s.close();

        }

        //send response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        //send response

    }
}
