package social.controller;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.data.DataUtils;
import social.model.Person;

/**
 * Renders a contacts page.
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
        EntityManager entityManager = (EntityManager) request.getAttribute("entitymanager");

        String search = request.getParameter("search");
        Long id = ((Person) request.getAttribute("user")).getId();
        int maxResults = (int) getServletContext().getAttribute("maxresults");

        int page = DataUtils.validatePageNumber(request.getParameter("page"),
                DataUtils.getContactsCount(entityManager, id, search),
                maxResults);

        request.setAttribute("users",
                DataUtils.getContacts(entityManager, id, search, page * maxResults, maxResults));
        request.setAttribute("page", page);
        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/embedded/contacts.jsp").forward(request, response);
    }
}
