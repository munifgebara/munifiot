package br.com.munif.framework.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author munif
 */
public class MunifPersistence {

    private static MunifPersistence instance = new MunifPersistence();

    public ThreadLocal<EntityManager> tlem = new ThreadLocal<>();

    public ThreadLocal<String> ip = new ThreadLocal<>();

    public ThreadLocal<String> login = new ThreadLocal<>();

    public void destroy() {
        System.out.println("------------> Finalizando Persistencia");
        emf.close();
    }

    public static MunifPersistence getInstance() {
        return instance;
    }
    private final EntityManagerFactory emf;

    private MunifPersistence() {
        System.out.println("------------> Criando Persistencia");
        emf = Persistence.createEntityManagerFactory("munifiotPU");
    }

    public EntityManager getEntityManager() {
        if (tlem.get() == null) {
            tlem.set(emf.createEntityManager());
        }
        return tlem.get();
    }

    public void destroyEntityManager() {
        tlem.get().close();
        tlem.remove();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        emf.close();
    }

}
