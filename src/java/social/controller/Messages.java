package social.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.data.DataUtils;

/**
 *
 * @author Владимир
 */
@WebServlet(name = "Messages", urlPatterns = {"/personal/embedded/messages"})
public class Messages extends HttpServlet {

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

        String query;
        String countQuery;
        String view;
                
        switch (request.getParameter("box") + "") {
            case "outbox":
                query = "select user.sentMessages from Person user where user.login = :login";
                countQuery = "select count(user.sentMessages) from Person user where user.login = :login";
                break;
            case "inbox":
            default:
                query = "select user.receivedMessages from Person user where user.login = :login";
                countQuery = "select count(user.receivedMessages) from Person user where user.login = :login";
        }
        EntityManager manager = (EntityManager) request.getAttribute("manager");

        int page = DataUtils.getValidPage(request.getParameter("page"),
                (long) manager.createQuery(countQuery)
                        .setParameter("login", request.getRemoteUser())
                        .getSingleResult());

        int maxResults = DataUtils.getMaxResults();

        request.setAttribute("messages",
                manager.createQuery(query)
                        .setParameter("login", request.getRemoteUser())
                        .setMaxResults(maxResults).setFirstResult(page * maxResults)
                        .getResultList());
        request.setAttribute("page", page);

        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/embedded/messages.jsp").forward(request, response);
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

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Messages servlet";
    }

}
