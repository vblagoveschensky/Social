package social.controller;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
 * Checks if a user trying to restore a session yet exists, if so, opens a
 * transaction, closes the EntityManager instance after handling the request at
 * servlets.
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
        EntityManager entityManager = ((EntityManagerFactory) servletRequest.getServletContext().getAttribute("entitymanagerfactory")).createEntityManager();
        entityManager.getTransaction().begin();
        if (request.getRemoteUser() != null) {
            try {
                request.setAttribute("user",
                        entityManager.getReference(Person.class,
                                DataUtils.getUserId(entityManager, request.getRemoteUser())));
                request.setAttribute("entitymanager", entityManager);
                chain.doFilter(servletRequest, servletResponse);

            } catch (NoResultException exception) {
                request.getSession().setAttribute("loginerror", "login.doesnotexist");
                request.getServletContext().getRequestDispatcher("/logout").forward(request, response);
            }
            entityManager.close();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) {

    }
}
