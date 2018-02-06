package social.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import social.data.DataUtils;

/**
 * Represents a user.
 */
@Entity
public class Person extends Model {

    /**
     * Creates a new instance
     */
    public Person() {

    }

    /**
     * Creates a new instance with login and name set
     *
     * @param login login
     * @param name name
     */
    public Person(String login, String name) {
        this.login = login;
        this.name = name;
    }

    @Size(min = 1, message = "person.name.empty")
    @Column(nullable = false)
    private String name;

    /**
     * Retrieves the user's name
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 1, message = "person.login.empty")
    @Column(nullable = false, unique = true)
    private String login;

    /**
     * Retrieves the user's login
     *
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the user's login
     *
     * @param login login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    @Column(nullable = false)
    private String passwordHash;

    /**
     * Validates the user password using the specified digest algorithm.
     *
     * @param password password to validate
     * @param digestAlgorithm algorithm to use
     * @return true if the password is valid.
     */
    public boolean validatePassword(String password, String digestAlgorithm) {
        return passwordHash.equals(DataUtils.encrypt(password, digestAlgorithm));
    }

    /**
     * Sets the password. The new password will be available for validation
     * after the entity update.
     *
     * @param password
     * @param digestAlgorithm
     */
    public void setPassword(String password, String digestAlgorithm) {
        this.password = password;
        this.digestAlgorithm = digestAlgorithm;
        passwordHash = null; //Needed to make a change to fire @PreUpdate.
    }

    @OrderBy("sent DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.PERSIST)
    private List<Message> sentMessages = new ArrayList<>();

    /**
     * retrieves messages sent by user
     *
     * @return list of messages
     */
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    @OrderBy("sent DESC")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "recipients")
    private List<Message> receivedMessages = new ArrayList<>();

    /**
     * retrieves messages recieved by user
     *
     * @return list of messages
     */
    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    /**
     * Sends a text message to recipients.
     *
     * @param recipients
     * @param text
     */
    public void sendMessage(List<Person> recipients, String text) {
        getSentMessages().add(new Message(this, recipients, text));
    }

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserGroup> groups = new ArrayList<>();

    /**
     * Retrieves a list of groups the user belongs to
     *
     * @return list of groups
     */
    public List<UserGroup> getGroups() {
        return groups;
    }

    @PreRemove
    private void preRemove() {
        getSentMessages().forEach(message -> message.setSender(null));
        getReceivedMessages().forEach(message
                -> message.getRecipients().remove(this));
    }

    @Size(min = 1, message = "person.password.empty")
    @Transient
    private String password;

    @Transient
    private String digestAlgorithm;

    @PrePersist
    @PreUpdate
    private void encryptPassword() {
        if (password != null && digestAlgorithm != null) {
            passwordHash = DataUtils.encrypt(password, digestAlgorithm);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

}
