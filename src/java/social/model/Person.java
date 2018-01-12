package social.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import social.data.DataUtils;

/**
 *
 * @author Владимир
 */
@Entity
public class Person extends Model {

    public Person() {

    }

    public Person(String login, String name) {
        this.login = login;
        this.name = name;
    }

    
    @Size(min = 1, message = "Name can not be empty.")
    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 1, message = "Login can not be empty.")
    @Column(nullable = false, unique = true)
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OrderBy("sent DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.PERSIST)
    private Set<Message> sentMessages = new LinkedHashSet<>();

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    @OrderBy("sent DESC")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "recipients")
    private Set<Message> receivedMessages = new LinkedHashSet<>();

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }
    
    public void sendMessage(Set<Person> recipients, String text) {
        getSentMessages().add(new Message(this, recipients, text));
    }

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<UserGroup> groups = new HashSet<>();

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = (HashSet<UserGroup>) groups;
    }

   

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

   

}
