package social.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.data.DataUtils;
import social.model.Person;

/**
 *
 * @author Владимир
 */
@WebServlet(name = "Unregister", urlPatterns = {"/personal/unregister"})
public class Unregister extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        getServletContext().getRequestDispatcher("/unregister.jsp").forward(request, response);
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
        EntityManager manager = (EntityManager) request.getAttribute("entitymanager");
        Person user = (Person) request.getAttribute("user");
        if (!DataUtils.encrypt(request.getParameter("password"),
                getServletContext().getInitParameter("digestAlgorithm")).equals(user.getPassword())) {
            request.setAttribute("passworderror", "Incorrect password.");
            manager.getTransaction().setRollbackOnly();
        }
        
        try {
            user.getSentMessages().forEach(message -> message.setSender(null));
            user.getReceivedMessages().forEach(message
                    -> message.getRecipients().remove(user));
            manager.remove(user);
            manager.getTransaction().commit();
            response.sendRedirect(getServletContext().getContextPath());
        } catch (RollbackException exception) {
            doGet(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Unregister servlet";
    }
    
}
