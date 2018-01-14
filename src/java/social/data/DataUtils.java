package social.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import social.model.Message;
import social.model.Person;

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

    public static String encrypt(String secret, String digestAlgorithm) {
        try {
            byte[] hash = MessageDigest.getInstance(digestAlgorithm).digest(secret.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder(hash.length * 2);
            Formatter formatter = new Formatter(stringBuilder);
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new ProviderException(String.format("Selected digest algorithm (%s) not supported.", digestAlgorithm), ex);
        }
    }

    public static String validatePassword(String password) {
        if (password.length() == 0) {
            return "Password can not be empty";
        } else {
            return null;
        }
    }

    public static int validatePageNumber(String txtPageNumber, long count, int maxResults) {
        int page = parseUnsignedIntOrZero(txtPageNumber);
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

    protected static long getCount(Query countQuery) {
        return (long) countQuery.getSingleResult();
    }

    public static long getMessagesCount(EntityManager entityManager, long id, String box) {
        return getCount(buildQuery(entityManager, box, true, id, false, null, null));
    }

    public static long getContactsCount(EntityManager entityManager, long id, String search) {
        return getCount(buildQuery(entityManager, null, true, id, true, search, false));
    }

    protected static List getPage(Query query, int first, int max) {
        return query.setFirstResult(first).setMaxResults(max).getResultList();
    }

    public static List<Message> getMessages(EntityManager entityManager, long id, String box, int first, int max) {
        return getPage(buildQuery(entityManager, box, false, id, false, null, null), first, max);
    }

    public static List<Person> getContacts(EntityManager entityManager, long id, String search, int first, int max) {
        return getPage(buildQuery(entityManager, null, false, id, true, search, false), first, max);
    }

    public static Long getUserId(EntityManager entityManager, String login) {
        return (Long) buildQuery(entityManager, "id", false, null, null, login, true).getSingleResult();
    }

    protected static Query buildQuery(EntityManager entityManager,
            String field, boolean count, Long id, Boolean excludeId, String search, Boolean strict) {
        String what = "user";
        if (field != null) {
            what += ("." + field);
        }

        if (count) {
            what = String.format("count(%s)", what);
        }

        List<String> where = new ArrayList();

        if (id != null) {
            String condition = excludeId ? " != " : " = ";
            where.add("user.id" + condition + ":id");
        }

        if (search != null) {
            if (strict) {
                where.add("user.login = :search");
            } else {
                search = "%" + search + "%";
                where.add("(user.login like :search or user.name like :search)");
            }
        }

        Query query = entityManager.createQuery(String.format("select %s from Person user where %s",
                what, String.join(" and ", where)));

        if (id != null) {
            query.setParameter("id", id);
        }

        if (search != null) {
            query.setParameter("search", search);
        }

        return query;
    }

}
