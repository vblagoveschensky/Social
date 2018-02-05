package social.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

/**
 *Represents a group of users
 */
@Entity
public class UserGroup extends Model {

    /**
     *default constructor
     */
    public UserGroup() {
    }

    /**
     *constructor
     * @param name name of the group
     */
    public UserGroup(String name) {
        this.name = name;
    }

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    /**
     *retrieves the name of the group
     * @return name of the group
     */
    public String getName() {
        return name;
    }

    /**
     *sets a new name for the group
     * @param name name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private List<Person> members = new ArrayList<>();

    /**
     *retrieves set of members
     * @return set of members
     */
    public List<Person> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
