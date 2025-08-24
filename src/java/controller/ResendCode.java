package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ResendCode", urlPatterns = {"/ResendCode"})
public class ResendCode extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        HttpSession ses = request.getSession();
        String email = (String) ses.getAttribute("email");

        System.out.println(email);

        if (email == null) {
            responseObject.addProperty("message", "Session expired or user not logged in.");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria criteria = s.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            List<User> userList = criteria.list();

            if (!userList.isEmpty()) {
                User u = userList.get(0); // load existing user

                // generate new verification code
                final String code = Util.generatedCode();
                u.setVerification(code);

                // begin transaction and update
                Transaction tx = s.beginTransaction();
                s.update(u);
                tx.commit();

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Code Resent");

                // send email
                new Thread(() -> Mail.sendMail(email, "Verification Code", code)).start();
            } else {
                responseObject.addProperty("message", "User not found.");
            }

            s.close();
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
