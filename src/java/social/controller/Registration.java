package social.controller;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import social.model.Person;
import social.model.UserGroup;

/**
 * Renders the registration page, handles its input.
 */
@WebServlet(name = "Registration", urlPatterns = {"/registration"})
public class Registration extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Renders the registration page.
     *Renders the registration page.
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
        getServletContext().getRequestDispatcher("/registration.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Handles the page input,
     * performs automatic login after registration.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager entityManager = ((EntityManagerFactory) getServletContext().getAttribute("entitymanagerfactory"))
                .createEntityManager();
        entityManager.getTransaction().begin();

        UserGroup users;
        try {
            users = (UserGroup) entityManager.createQuery("from UserGroup ugroup where ugroup.name = 'Users'")
                    .getSingleResult();
        } catch (NoResultException exception) {
            users = new UserGroup("Users");
            entityManager.persist(users);
        }

        Person newcomer = new Person(request.getParameter("login"),
                request.getParameter("name"));

        newcomer.getGroups().add(users);

        newcomer.setPassword(request.getParameter("password"),
                getServletContext().getInitParameter("digestAlgorithm"));

        try {
            entityManager.persist(newcomer);
            entityManager.getTransaction().commit();
            if (request.getRemoteUser() != null) {
                request.logout();
            }
            request.login(request.getParameter("login"), request.getParameter("password"));
            response.sendRedirect(getServletContext().getContextPath());
        } catch (ConstraintViolationException exception) {
            exception.getConstraintViolations().forEach(violation -> {
                request.setAttribute(violation.getPropertyPath().toString() + "error", violation.getMessage());
            });
            doGet(request, response);
        } catch (RollbackException exception) {
            request.setAttribute("loginerror", "person.login.duplicate");
            doGet(request, response);
        }
        entityManager.close();
    }
}
