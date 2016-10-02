package br.com.munif.framework.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;

/**
 * Conjunto de métodos para facilitar tarefas em entidades.
 * @author munif
 */
public class EntityUtils {
    /**
     * Obtem o valor da chave primária de uma entidade. O método procura o
     * atributo com a anotação ID na classe e nas superclasses
     *
     * @param entity entidade que deseja-se a chave
     * @return valor da chave
     */
    public static Long getId(MultiTenancySuperEntity entity) {
        return entity.getId();
    }

    /**
     * Obtém o valor do atributo a partir de seu nome e do objeto que o contém
     *
     * @param entity
     * @param attrName
     * @return
     */
    public static Object getAttributeValue(Object entity, String attrName) {
        try {
            Field f = entity.getClass().getDeclaredField(attrName);
            f.setAccessible(true);
            return f.get(entity);
        } catch (Exception ex) {
            System.out.println("Field not found " + attrName + " in class " + entity.getClass().getName()+" "+ ex);
        }
        return null;
    }



    /**
     * Método recursivo para descobrir todos os atributos da entidade, incluindo
     * os das superclasses, se existirem
     *
     * @param classe classe da entidade
     * @return um ArrayList com os atributos (Fields) da entidade
     */
    public static List<Field> getFields(Class classe) {
        List<Field> lista = new ArrayList<>();
        if (!classe.getSuperclass().equals(Object.class)) {
            lista.addAll(getFields(classe.getSuperclass()));
        }
        for (Field f : classe.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            lista.add(f);
        }
        return lista;
    }

    /**
     * Método que retorna o primeiro atributo da classe de entidade. Não retorna
     * atributos estáticos. Evita retorna atributos com a anotação
     * Id, isto é, caso exista algum atributo não estático sem esta
     * anotação, este será retornado. Este método é destinado a construção de
     * consultas genéricas.
     *
     * @param classe Classe da entidade.
     * @return
     */
    public static Field firtField(Class classe) {
        Field f = null;
        for (Field atributo : getFields(classe)) {
            if (Modifier.isStatic(atributo.getModifiers())) {
                continue;
            }
            if (f == null) {
                f = atributo;
            }
            if (!atributo.isAnnotationPresent(Id.class)) {
                return atributo;
            } else if (f == null) {
                f = atributo;
            }
        }
        return f;
    }
    
}
