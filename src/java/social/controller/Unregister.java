package social.controller;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.model.Person;

/**
 * Handles unregister requests.
 */
@WebServlet(name = "Unregister", urlPatterns = {"/personal/unregister"})
public class Unregister extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Renders the unregister page.
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
        getServletContext().getRequestDispatcher("/unregister.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Handles the unregister
     * request.
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
        if (user.validatePassword(request.getParameter("password"), digestAlgorithm)) {
            manager.remove(user);
            manager.getTransaction().commit();
            response.sendRedirect(getServletContext().getContextPath());
        } else {
            request.setAttribute("passworderror", "person.password.incorrect");
            manager.getTransaction().setRollbackOnly();
        }
    }
}
