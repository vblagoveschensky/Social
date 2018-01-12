package social.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
@WebServlet(name = "Messenger", urlPatterns = {"/personal/messenger"})
public class Messenger extends HttpServlet {

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
        getServletContext().getRequestDispatcher("/messenger.jsp").forward(request, response);
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
        Set<Person> recipients = new HashSet<>();
        if (request.getParameter("recipients") != null) {
            for (String id : request.getParameterValues("recipients")) {
                try {
                    recipients.add(manager.getReference(Person.class, Long.parseUnsignedLong(id)));
                } catch (EntityNotFoundException exception) {

                }
            }
            if (recipients.size() < request.getParameterValues("recipients").length) {
                manager.getTransaction().setRollbackOnly();
                request.setAttribute("senderror", "Some recipients do not exist, removed. Please try again.");
            }
        } else {
            manager.getTransaction().setRollbackOnly();
            request.setAttribute("senderror", "Please select a recipient.");
        }
        ((Person) request.getAttribute("user")).sendMessage(recipients,
                request.getParameter("text"));
        try {
            manager.getTransaction().commit();
        } catch (RollbackException exception) {
            request.setAttribute("recipients", recipients);
        }
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Messenger servlet";
    }

}
