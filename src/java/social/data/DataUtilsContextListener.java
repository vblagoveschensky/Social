package social.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static social.data.DataUtils.parseUnsignedIntOrZero;
import social.model.UserGroup;

public class DataUtilsContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("entitymanagerfactory", Persistence.createEntityManagerFactory("SocialPU"));
        context.setAttribute("maxresults", parseUnsignedIntOrZero(context.getInitParameter("maxResults")));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((EntityManagerFactory) sce.getServletContext().getAttribute("entitymanagerfactory")).close();
    }
}
