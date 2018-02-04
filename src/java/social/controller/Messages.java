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
 * Renders a folder-with-messages page.
 */
@WebServlet(name = "Messages", urlPatterns = {"/personal/embedded/messages"})
public class Messages extends HttpServlet {

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
        String folder = request.getParameter("folder");
        folder = "sentMessages".equals(folder) ? folder : "receivedMessages";
        EntityManager entityManager = (EntityManager) request.getAttribute("entitymanager");
        Long id = ((Person) request.getAttribute("user")).getId();
        int maxResults = (int) getServletContext().getAttribute("maxresults");
        int page = DataUtils.validatePageNumber(request.getParameter("page"),
                DataUtils.getMessagesCount(entityManager, id, folder),
                maxResults);
        request.setAttribute("messages",
                DataUtils.getMessages(entityManager, id, folder, page * maxResults, maxResults));
        request.setAttribute("page", page);
        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/embedded/messages.jsp").forward(request, response);
    }
}
