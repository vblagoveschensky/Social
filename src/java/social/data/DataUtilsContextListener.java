package social.data;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static social.data.DataUtils.parseUnsignedIntOrZero;

/**
 * Maintains context-scope attributes related to data manipulation
 */
public class DataUtilsContextListener implements ServletContextListener {

    /**
     * Initializes context-scope attributes related to data manipulation
     *
     * @param sce ServletContextEvent instance
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("entitymanagerfactory", Persistence.createEntityManagerFactory("SocialPU"));
        context.setAttribute("maxresults", parseUnsignedIntOrZero(context.getInitParameter("maxResults")));
    }

    /**
     * Closes open resources
     *
     * @param sce ServletContextEvent instance
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((EntityManagerFactory) sce.getServletContext().getAttribute("entitymanagerfactory")).close();
    }
}
