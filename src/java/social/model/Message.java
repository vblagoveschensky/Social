package social.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a message.
 */
@Entity
public class Message extends Model {

    /**
     * default constructor
     */
    public Message() {

    }

    /**
     * constructor
     *
     * @param sender sender
     * @param recipients set of recipients
     * @param text text of the message
     */
    public Message(Person sender, List<Person> recipients, String text) {
        this.sender = sender;
        this.recipients = recipients;
        this.text = text;
    }

    @ManyToOne
    private Person sender;

    /**
     * retrieves the sender of the message
     *
     * @return sender of the message
     */
    public Person getSender() {
        return sender;
    }

    /**
     * sets the sender of the message
     *
     * @param sender sender of the message to be set
     */
    public void setSender(Person sender) {
        this.sender = sender;
    }

    @Column(nullable = false)
    private String text;

    /**
     * retrieves the text of the message
     *
     * @return text of the message
     */
    public String getText() {
        return text;
    }

    /**
     * sets the text of the message
     *
     * @param text text of the message
     */
    public void setText(String text) {
        this.text = text;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Person> recipients = new ArrayList<>();

    /**
     * retrieves the set of the recipients
     *
     * @return set of the recipients
     */
    public List<Person> getRecipients() {
        return recipients;
    }

    /**
     * sets the set of the recipients
     *
     * @param recipients set of the recipients to be set
     */
    public void setRecipients(List<Person> recipients) {
        this.recipients = recipients;
    }

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;

    /**
     * retrieves the date and time of the message
     *
     * @return date and time of the message
     */
    public Date getSent() {
        return sent;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
