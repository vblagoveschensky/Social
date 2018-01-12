package social.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import social.data.DataUtils;
import social.model.Person;
import social.model.UserGroup;

/**
 *
 * @author Владимир
 */
@WebServlet(name = "Personal", urlPatterns = {"/personal"})
public class Personal extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/personal.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager manager = (EntityManager) request.getAttribute("manager");

        String algorithm = getServletContext().getInitParameter("digestAlgorithm");
        Person user = (Person) request.getAttribute("user");
        if (request.getParameter("name") != null) {
            user.setName(request.getParameter("name"));
        } else {
            if (!DataUtils.encrypt(request.getParameter("oldpassword"), algorithm).equals(user.getPassword())) {
                request.setAttribute("oldpassworderror", "Incorrect password.");
                manager.getTransaction().setRollbackOnly();
            }
            String passwordError = DataUtils.validatePassword(request.getParameter("password"));
            if (passwordError == null) {
                user.setPassword(DataUtils.encrypt(request.getParameter("password"), algorithm));
            } else {
                request.setAttribute("passworderror", passwordError);
                manager.getTransaction().setRollbackOnly();
            }
            if (!manager.getTransaction().getRollbackOnly()) {
                request.setAttribute("oldpassworderror", "Password changed.");
            }
        }
        try {
            manager.getTransaction().commit();
        } catch (RollbackException exception) {
            if (exception.getCause() != null) {
                ConstraintViolationException cause = (ConstraintViolationException) exception.getCause();
                for (ConstraintViolation violation : cause.getConstraintViolations()) {
                    request.setAttribute(violation.getPropertyPath().toString() + "error", violation.getMessage());
                }
            }
            request.setAttribute("user", manager.getReference(Person.class, user.getId()));

        }
        doGet(request, response);
    }
}
