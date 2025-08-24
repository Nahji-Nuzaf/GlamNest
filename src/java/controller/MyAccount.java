/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
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

@WebServlet(name = "MyAccount", urlPatterns = {"/MyAccount"})
public class MyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("user") != null) {
            User user = (User) ses.getAttribute("user");

            JsonObject responseObject = new JsonObject();

            responseObject.addProperty("email", user.getEmail());
            responseObject.addProperty("firstName", user.getFirst_name());
            responseObject.addProperty("lastName", user.getLast_name());
            responseObject.addProperty("mobile", user.getMobile());
            responseObject.addProperty("password", user.getPassword());

            String since = new SimpleDateFormat("MMM yyyy").format(user.getCreated_at());
            responseObject.addProperty("since", since);

            Gson gson = new Gson();
            SessionFactory sf = HibernateUtil.getSessionFactory();

            Session s = sf.openSession();
            Criteria c = s.createCriteria(Address.class);
            c.add(Restrictions.eq("user", user));
            if (!c.list().isEmpty()) {
                List<Address> addressList = c.list();
                responseObject.add("addressList", gson.toJsonTree(addressList));
            }

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject userData = gson.fromJson(request.getReader(), JsonObject.class);

        String firstName = userData.get("firstName").getAsString();
        String lastName = userData.get("lastName").getAsString();
        String mobile = userData.get("mobile").getAsString();
        String lineOne = userData.get("lineOne").getAsString();
        String lineTwo = userData.get("lineTwo").getAsString();
        String postalCode = userData.get("postalCode").getAsString();
        int cityId = userData.get("cityId").getAsInt();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "First Name can not be empty!");
        } else if (lastName.isEmpty()) {
            responseObject.addProperty("message", "Last Name can not be empty!");
        } else if (mobile.isEmpty()) {
            responseObject.addProperty("message", "Mobile can not be empty!");
        } else if (lineOne.isEmpty()) {
            responseObject.addProperty("message", "Enter address line one");
        } else if (lineTwo.isEmpty()) {
            responseObject.addProperty("message", "Enter address line two");
        } else if (cityId == 0) {
            responseObject.addProperty("message", "Select a city");
        } else if (postalCode.isEmpty()) {
            responseObject.addProperty("message", "Enter your postal code");
        } else if (!Util.isCodeValid(postalCode)) {
            responseObject.addProperty("message", "Enter correct postal code");
        } else {

            HttpSession ses = request.getSession();

            if (ses.getAttribute("user") != null) {

                User u = (User) ses.getAttribute("user");

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                Criteria c = s.createCriteria(User.class);
                c.add(Restrictions.eq("email", u.getEmail()));//session user email

                if (!c.list().isEmpty()) {

                    User u1 = (User) c.list().get(0); //db user
                    u1.setFirst_name(firstName);
                    u1.setLast_name(lastName);
                    u1.setMobile(mobile);

                    //
                    Address existingAddress = null;
                    Criteria c1 = s.createCriteria(Address.class);
                    c1.add(Restrictions.eq("user", u1));

                    if (!c1.list().isEmpty()) {
                        existingAddress = (Address) c1.list().get(0);
                    }

                    City city = (City) s.load(City.class, cityId); // primary key search

                    if (existingAddress != null) {
                        existingAddress.setLineOne(lineOne);
                        existingAddress.setLineTwo(lineTwo);
                        existingAddress.setPostalCode(postalCode);
                        existingAddress.setCity(city);
                        existingAddress.setUser(u1);

                        s.update(existingAddress);
                    } else {
                        Address address = new Address();
                        address.setLineOne(lineOne);
                        address.setLineTwo(lineTwo);
                        address.setPostalCode(postalCode);
                        address.setCity(city);
                        address.setUser(u1);

                        s.save(address);
                    }

                    //
                    //session-management
                    ses.setAttribute("user", u1);
                    //session-management-end

                    s.merge(u1);
                    s.beginTransaction().commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "User profile details update successfully!");
                    s.close();

                }

            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject passwordData = gson.fromJson(request.getReader(), JsonObject.class);

        String currentPassword = passwordData.get("currentPassword").getAsString();
        String newPassword = passwordData.get("newPassword").getAsString();
        String confirmNewPassword = passwordData.get("confirmNewPassword").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (newPassword.isEmpty()) {
            responseObject.addProperty("message", "New Password can not be empty!");
        } else if (!Util.isPasswordValid(newPassword)) {
            responseObject.addProperty("message", "Invalid New Pasword");
        } else if (confirmNewPassword.isEmpty()) {
            responseObject.addProperty("message", "Confirm New Password");
        } else if (!Util.isPasswordValid(confirmNewPassword)) {
            responseObject.addProperty("message", "Invalid Password");
        } else if (!newPassword.equals(confirmNewPassword)) {
            responseObject.addProperty("message", "Passwords do not match");
        } else {

            HttpSession ses = request.getSession();

            if (ses.getAttribute("user") != null) {

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                try {
                    User u = (User) ses.getAttribute("user");

                    Transaction tr = s.beginTransaction();

                    Criteria c = s.createCriteria(User.class);
                    c.add(Restrictions.eq("email", u.getEmail()));//session user email
                    c.add(Restrictions.eq("password", currentPassword));//session user password

                    if (!c.list().isEmpty()) {

                        User u1 = (User) c.list().get(0); //db user
                        u1.setPassword(newPassword);

                        //session-management
                        ses.setAttribute("user", u1);
                        //session-management-end

                        s.update(u1);
                        tr.commit();

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Password update successfully!");

                    }
                } finally {
                    s.close();
                }

            } else {
                responseObject.addProperty("message", "User not logged in.");
            }
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }
}
