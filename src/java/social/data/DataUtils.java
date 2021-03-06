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
     *Represents data needed for pagination.
     */
    public static class Pagination {

        private int pageNumber;
        private int resultsCount;
        private int maxResults;

        /**
         *Creates a new Pagination object.
         * @param txtPageNumber text representation of requested page number
         * @param count total amount of records
         * @param maxResults maximum number of results on a page
         */
        public Pagination(String txtPageNumber, long count, int maxResults) {
            this.maxResults = maxResults;
            resultsCount = maxResults;
            pageNumber = parseUnsignedIntOrZero(txtPageNumber);
            if (maxResults > 0) {
                int pages = (int) (count / maxResults);
                if (pageNumber >= pages) {
                    resultsCount = (int) (count % maxResults);
                    pageNumber = pages;
                }
            } else {
                pageNumber = 0;
            }
        }

        /**
         *
         * @return number of the page
         */
        public int getPageNumber() {
            return pageNumber;
        }

        /**
         *
         * @return offset to use in query to display the page
         */
        public int getOffset() {
            return pageNumber * maxResults;
        }

        /**
         *
         * @return maxresults to use in query to display the page
         */
        public int getMaxResults() {
            return resultsCount;
        }
    }

    private static long getCount(Query countQuery) {
        return (long) countQuery.getSingleResult();
    }

    /**
     * Retrieves the number of messages in a user's DataUtils
     *
     * @param entityManager EntityManager instance to use
     * @param id id of user, whose messages to be counted
     * @param folder name of the folder
     * @return number of messages
     */
    public static long getMessagesCount(EntityManager entityManager, long id, String folder) {
        return getCount(buildQuery(entityManager, folder, false, true, id, false, null, null, null));
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
        return getCount(buildQuery(entityManager, null, null, true, id, true, search, false, null));
    }

    private static List getPage(Query query, int first, int max) {
        return query.setFirstResult(first).setMaxResults(max).getResultList();
    }

    /**
     * Retrieves messages from a user's folder
     *
     * @param entityManager EntityManager instance to use
     * @param id user's id
     * @param folder name of the folder
     * @param first number of the first record
     * @param max maximum number of records per page
     * @return list of messages
     */
    @SuppressWarnings("unchecked")
    public static List<Message> getMessages(EntityManager entityManager, long id, String folder, int first, int max) {
        return getPage(buildQuery(entityManager, folder, true, false, id, false, null, null, "sent DESC"), first, max);
    }

    /**
     * Retrieves user's contacts
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
        return getPage(buildQuery(entityManager, null, null, false, id, true, search, false, null), first, max);
    }

    /**
     * Retrieves id of a user by his login
     *
     * @param entityManager EntityManager instance to use
     * @param login the user's login
     * @return id of the user
     */
    public static Long getUserId(EntityManager entityManager, String login) {
        return (Long) buildQuery(entityManager, "id", false, false, null, null, login, true, null).getSingleResult();
    }

    private static Query buildQuery(EntityManager entityManager,
            String field, Boolean join, boolean count, Long id, Boolean excludeId, String search,
            Boolean strict, String order) {
        final String JOINED = "joined";
        String what = "user";
        String from = "Person user";
        if (field != null) {
            what += ("." + field);
            if (join) {
                from += (String.format(" join %s %s", what, JOINED));
            }
        }

        if (count) {
            what = String.format("count(%s)", what);
        }

        List<String> where = new ArrayList<>();

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

        if (order == null) {
            order = "";
        } else {
            order = String.format("order by %s.%s", JOINED, order);
        }

        Query query = entityManager.createQuery(String.format("select distinct %s from %s where %s %s",
                what, from, String.join(" and ", where), order));

        if (id != null) {
            query.setParameter("id", id);
        }

        if (search != null) {
            query.setParameter("search", search);
        }

        return query;
    }

}
