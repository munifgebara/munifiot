package br.com.munif.framework.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author munif
 */
@EntityListeners(MultiTenancyListener.class)
@MappedSuperclass
public class MultiTenancySuperEntity implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private TenancyGroup tenancyGroup;
    private static long lastid = 0;

    public MultiTenancySuperEntity() {
        id = nextId();
    }

    public final synchronized long nextId() {
        long nextId = System.currentTimeMillis() * 1000;
        if (nextId == lastid) {
            nextId++;
        }
        lastid = nextId;
        return nextId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonGetter(value = "class")
    public String getClazz() {
        return this.getClass().getCanonicalName();
    }

    public TenancyGroup getTenancyGroup() {
        return tenancyGroup;
    }

    public void setTenancyGroup(TenancyGroup tenancyGroup) {
        this.tenancyGroup = tenancyGroup;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MultiTenancySuperEntity other = (MultiTenancySuperEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
