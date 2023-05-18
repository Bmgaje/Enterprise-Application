package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.persistence.Id;
import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@EntityListeners(PojoCompositeListener.class)
public abstract class PojoBaseCompositeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    protected int id;

    @Version
    protected int version;

    @Column(updatable = false)
    @CreationTimestamp
    protected LocalDateTime created;

    @Column
    @UpdateTimestamp
    protected LocalDateTime updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        return prime * result + Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof PojoBaseCompositeKey otherPojoBase) {
            return Objects.equals(this.getId(), otherPojoBase.getId());
        }
        return false;
    }
}
