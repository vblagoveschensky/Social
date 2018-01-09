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
@WebServlet(name = "Contacts", urlPatterns = {"/personal/embedded/contacts"})
public class Contacts extends HttpServlet {

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
        String query = "from Person user where user.login != :login and (user.login like :search or user.name like :search)";
        EntityManager manager = (EntityManager) request.getAttribute("manager");

        String search = request.getParameter("search");
        if (search == null) {
            search = "%";
        } else {
            search = "%" + search + "%";
        }

        int page = DataUtils.getValidPage(request.getParameter("page"),
                (long) manager.createQuery(
                        "select count(user) " + query)
                        .setParameter("login", request.getRemoteUser())
                        .setParameter("search", search)
                        .getSingleResult());
        
        int maxResults = DataUtils.getMaxResults();

        request.setAttribute("users",
                manager.createQuery(query)
                        .setParameter("login", request.getRemoteUser())
                        .setParameter("search", search)
                        .setMaxResults(maxResults)
                        .setFirstResult(page * maxResults).getResultList());
        request.setAttribute("page", page);
        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/embedded/contacts.jsp").forward(request, response);
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
        return "Contacts servlet";
    }

}
