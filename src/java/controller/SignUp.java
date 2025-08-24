package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String firstName = user.get("firstName").getAsString();
        String lastName = user.get("lastName").getAsString();
        String email = user.get("email").getAsString();
        String mobile = user.get("mobile").getAsString();
        String password = user.get("password").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "First name cannot be empty");
        } else if (lastName.isEmpty()) {
            responseObject.addProperty("message", "Last name cannot be empty");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Email name cannot be empty");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please enter valid email");
        } else if (mobile.isEmpty()) {
            responseObject.addProperty("message", "Mobile cannot be Empty");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Password cannot be empty");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "Please enter valid password");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria criteria = s.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            if (!criteria.list().isEmpty()) {
                responseObject.addProperty("message", "This User already Exists!");
            } else {

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Successfully Registered.Please check your email for verification code");

                User u = new User();
                u.setFirst_name(firstName);
                u.setLast_name(lastName);
                u.setEmail(email);
                u.setMobile(mobile);
                u.setPassword(password);

                //RandomNumberTake
                final String code = Util.generatedCode();
                u.setVerification(code);

                u.setCreated_at(new Date());
                s.save(u);
                s.beginTransaction().commit();

                //Send Email
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "GlamNest - Verify Your Account", code);
                    }
                }).start();
                //Send Email

                //session management
                HttpSession ses = request.getSession();
                ses.setAttribute("email", email);
                //session management

            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
