package social.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.servlet.ServletContext;

/**
 *
 * @author Владимир
 */
public class DataUtils {

    private DataUtils() {

    }

    public static int parseUnsignedIntOrZero(String string) {
        try {
            return Integer.parseUnsignedInt(string);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static String encrypt(String secret, String algorithm) {
        try {
            byte[] hash = MessageDigest.getInstance(algorithm).digest(secret.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder(hash.length * 2);
            Formatter formatter = new Formatter(stringBuilder);
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new ProviderException(String.format("Selected digest algorithm (%s) not supported.", algorithm), ex);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("No digest algorithm specified");
        }
    }

    public static String validatePassword(String password) {
        if (password.length() == 0) {
            return "Password can not be empty";
        } else {
            return null;
        }
    }

    public static int validatePageNumber(String strPageNumber, long count, int maxResults) {
        int page = parseUnsignedIntOrZero(strPageNumber);
        if (maxResults > 0) {
            int maxPage = (int) (count / maxResults);
            if (page > maxPage) {
                page = maxPage;
            }
        } else {
            page = 0;
        }
        return page;
    }

    public static Query buildQuery(EntityManager entityManager, boolean count, boolean other, String field, Long id, String search) {
        final String QUERY_STRING = "from Person user where user.id";
        final String QUERY_STRING_SEARCH = " and (user.login like :search or user.name like :search)";
        String queryString = "";
        if (field != null && !field.isEmpty()) {
            queryString = "user." + field;
            if (count) {
                queryString = String.format("select count(%s) ", queryString);
            } else {
                queryString = String.format("select %s ", queryString);
            }
        } else if (count) {
            queryString = "select count(user) ";
        }
        queryString += QUERY_STRING;
        
        if (other) {
            queryString += " != :id";
        } else {
            queryString += " = :id";
        }

        Query query;
        if (search == null) {
            query = entityManager.createQuery(queryString);
        } else {
            query = entityManager.createQuery(queryString + QUERY_STRING_SEARCH)
                    .setParameter("search", "%" + search + "%");
        }
        return query.setParameter("id", id);
    }
}