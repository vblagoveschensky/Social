package social.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Formatter;
import javax.persistence.Query;
import javax.servlet.ServletContext;

/**
 *
 * @author Владимир
 */
public class DataUtils {

    private DataUtils() {

    }

    private static EntityManagerFactory factory;

    private static String algorithm;

    private static int maxResults;

    private final static String DEFAULTDIGESTALGORITHM = "SHA-256";

    public static void init(ServletContext context) {
        factory = Persistence.createEntityManagerFactory("SocialPU");
        algorithm = context.getInitParameter("digestAlgorithm");
        if (algorithm == null || algorithm.isEmpty()) {
            algorithm = DEFAULTDIGESTALGORITHM;
        }
        maxResults = parseUnsignedIntOrZero(context.getInitParameter("maxResults"));
    }

    public static int parseUnsignedIntOrZero(String string) {
        try {
            return Integer.parseUnsignedInt(string);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void unInit() {
        factory.close();
    }

    public static String encrypt(String secret, String algorithm) throws NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance(algorithm).digest(secret.getBytes(StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder(hash.length * 2);
        Formatter formatter = new Formatter(stringBuilder);
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return stringBuilder.toString();
    }

    public static String validatePassword(String password) {
        if (password.length() == 0) {
            return "Password can not be empty";
        } else {
            return null;
        }
    }

    public static String encrypt(String secret) {
        try {
            return encrypt(secret, algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new ProviderException(String.format("Digest algorithm (%s) not available.", algorithm), ex);
        }
    }

    public static int getMaxResults() {
        return maxResults;
    }
    
    public static int getValidPage(String page, long count) {
        return getValidPage(parseUnsignedIntOrZero(page), count);
    }

    public static int getValidPage(int page, long count) {
        if (maxResults > 0) {
            int maxPage = (int) (count / maxResults);
            if (page > maxPage) {
                page = maxPage;
            }
        }
        if (page >= 0) {
            return page;
        } else {
            return 0;
        }
    }

}
