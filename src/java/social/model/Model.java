package social.model;

import java.io.Serializable;
import javax.persistence.*;

@MappedSuperclass
public abstract class Model implements Serializable {
    protected static final long serialVersionUID = 1L;
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
