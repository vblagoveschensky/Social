package social.controller;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import social.model.Person;

/**
 * Personal page servlet.
 */
@WebServlet(name = "Personal", urlPatterns = {"/personal"})
public class Personal extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Renders the user's homepage.
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
     * Handles the HTTP <code>POST</code> method. Handles requests related to
     * user information changing.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager manager = (EntityManager) request.getAttribute("entitymanager");
        String digestAlgorithm = getServletContext().getInitParameter("digestAlgorithm");
        Person user = (Person) request.getAttribute("user");
        if (request.getParameter("name") != null) {
            user.setName(request.getParameter("name"));
        } else {
            if (!user.validatePassword(request.getParameter("oldpassword"), digestAlgorithm)) {
                request.setAttribute("oldpassworderror", "person.password.incorrect");
                manager.getTransaction().setRollbackOnly();
            } else {
                user.setPassword(request.getParameter("password"), digestAlgorithm);
            }
        }

        try {
            manager.getTransaction().commit();
            if (request.getParameter("password") != null) {
                request.setAttribute("oldpassworderror", "person.password.changed");
            }
        } catch (RollbackException exception) {
            if (exception.getCause() != null) {
                ConstraintViolationException cause = (ConstraintViolationException) exception.getCause();
                cause.getConstraintViolations().forEach(violation -> {
                    request.setAttribute(violation.getPropertyPath().toString() + "error", violation.getMessage());
                });
            }
            request.setAttribute("user", manager.getReference(Person.class, user.getId()));
        }
        doGet(request, response);
    }
}
