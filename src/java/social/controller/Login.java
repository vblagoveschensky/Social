package social.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Renders the login page, handles login failure.
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    
    /**
     * Handles the HTTP <code>GET</code> method.
     * Renders the login page.
     * Cleans related session-scope attributes after page rendering.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        request.getSession().removeAttribute("loginerror");
        request.getSession().removeAttribute("login");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Is invoked if login fails on the container level.
     * Sets related session-scope attributes to fill the login page next time.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().setAttribute("loginerror", "login.failed");
        request.getSession().setAttribute("login", request.getParameter("j_username"));
        response.sendRedirect(getServletContext().getContextPath());
    }
}
