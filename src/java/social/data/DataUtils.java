package social.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import java.util.Formatter;
import java.util.List;
import javax.persistence.Query;
import social.model.Message;
import social.model.Person;

/**
 * Provides static data manipulation methods
 */
public class DataUtils {

    private DataUtils() {

    }

    static int parseUnsignedIntOrZero(String string) {
        try {
            return Integer.parseUnsignedInt(string);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    /**
     * Encrypts a string with a hash function
     *
     * @param secret string to encrypt
     * @param digestAlgorithm algorithm to use
     * @return encrypted string
     * @exception ProviderException if the algorithm is not supported
     */
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

    /**
     * Validates and corrects a requested page number for pagination
     *
     * @param txtPageNumber page number text representation
     * @param count number of records
     * @param maxResults maximum number of records per page
     * @return a valid page number
     */
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

    private static long getCount(Query countQuery) {
        return (long) countQuery.getSingleResult();
    }

    /**
     * Retrieves the number of messages in a user's DataUtils
     * @param entityManager EntityManager instance to use
     * @param id id of user, whose messages to be counted
     * @param folder name of the folder
     * @return number of messages
     */
    public static long getMessagesCount(EntityManager entityManager, long id, String folder) {
        return getCount(buildQuery(entityManager, folder, true, id, false, null, null));
    }

    /**
     * Retrieves the number of user's contacts
     *
     * @param entityManager EntityManager instance to use
     * @param id of user, whose contacts to be counted
     * @param search search string
     * @return number of contacts
     */
    public static long getContactsCount(EntityManager entityManager, long id, String search) {
        return getCount(buildQuery(entityManager, null, true, id, true, search, false));
    }

    private static List getPage(Query query, int first, int max) {
        return query.setFirstResult(first).setMaxResults(max).getResultList();
    }

    /**
     * Retrieves messages from a user's folder
     * @param entityManager EntityManager instance to use
     * @param id user's id
     * @param folder name of the folder
     * @param first number of the first record
     * @param max maximum number of records per page
     * @return list of messages
     */
    @SuppressWarnings("unchecked")
    public static List<Message> getMessages(EntityManager entityManager, long id, String folder, int first, int max) {
        return getPage(buildQuery(entityManager, folder, false, id, false, null, null), first, max);
    }

    /**
     *
     * @param entityManager EntityManager instance to use
     * @param id user's id
     * @param search search string
     * @param first number of the first record
     * @param max maximum number of records per page
     * @return list of contacts
     */
    @SuppressWarnings("unchecked")
    public static List<Person> getContacts(EntityManager entityManager, long id, String search, int first, int max) {
        return getPage(buildQuery(entityManager, null, false, id, true, search, false), first, max);
    }

    /**
     * Retrieves id of a user by his login
     * @param entityManager EntityManager instance to use
     * @param login the user's login
     * @return id of the user
     */
    public static Long getUserId(EntityManager entityManager, String login) {
        return (Long) buildQuery(entityManager, "id", false, null, null, login, true).getSingleResult();
    }

    private static Query buildQuery(EntityManager entityManager,
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
