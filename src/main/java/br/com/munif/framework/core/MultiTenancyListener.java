package br.com.munif.framework.core;

import javax.persistence.PrePersist;

public class MultiTenancyListener {

    @PrePersist
    public void prePersist(MultiTenancySuperEntity entidade) {
        TenancyGroup tenancyGroup = null;
        if (tenancyGroup != null) {
            entidade.setTenancyGroup(tenancyGroup);
        }
    }
}
