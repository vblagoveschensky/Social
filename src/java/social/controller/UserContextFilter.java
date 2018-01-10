package social.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.data.DataUtils;
import social.model.Person;

/**
 *
 * @author Владимир
 */
@WebFilter(filterName = "UserContextFilter", urlPatterns = {"/personal/*"})
public class UserContextFilter implements Filter {

    public UserContextFilter() {
    }

    /**
     *
     * @param servletRequest The servlet servletRequest we are processing
     * @param servletResponse The servlet servletResponse we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        EntityManager manager = DataUtils.getEntityManager();
        manager.getTransaction().begin();
        if (request.getRemoteUser() != null) {
            try {
                request.setAttribute("user",
                        manager.getReference(Person.class,
                                manager.createQuery("select user.id from Person user where user.login = :login")
                                        .setParameter("login", request.getRemoteUser())
                                        .getSingleResult()));
                request.setAttribute("manager", manager);
                chain.doFilter(servletRequest, servletResponse);
                manager.close();
            } catch (NoResultException exception) {
                request.getSession().setAttribute("loginerror", "This login does not exist anymore.");
                request.getServletContext().getRequestDispatcher("/logout").forward(request, response);
            }
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {

        return ("UserContextFilter()");

    }

}
