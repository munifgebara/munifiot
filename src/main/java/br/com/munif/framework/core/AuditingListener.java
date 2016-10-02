package br.com.munif.framework.core;

import org.hibernate.envers.RevisionListener;

public class AuditingListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        AuditingRevision ar = (AuditingRevision) o;
        ar.setUserLogin(MunifPersistence.getInstance().login.get());
        ar.setIp(MunifPersistence.getInstance().ip.get());
    }
}
