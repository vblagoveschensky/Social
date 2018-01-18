package social.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *Represents an entity
 */
@MappedSuperclass
public abstract class Model implements Serializable {

    /**
     *Class version needed for serialization
     */
    protected static final long serialVersionUID = 1L;
        
    /**
     *Entity identity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     *Retrieve the id
     * @return id of the entity
     */
    public Long getId() {
        return id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return getClass().getName() + " [ id=" + id + " ]";
    }
}
