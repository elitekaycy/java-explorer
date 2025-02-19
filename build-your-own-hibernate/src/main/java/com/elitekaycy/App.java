package com.elitekaycy;

import java.util.Set;

import com.elitekaycy.interfaces.EntityManager;
import com.elitekaycy.interfaces.EntityScanner;
import com.elitekaycy.test.entity.Course;
import com.elitekaycy.utils.EntityGenerator;
import com.elitekaycy.utils.HibernateEntityScanner;
import com.elitekaycy.utils.SessionManagerFactory;

public class App {
    public static void main(String[] args) {

        // create db properties from args

        EntityScanner scanner = new HibernateEntityScanner("com.elitekaycy.test.entity");

        // scan the entities and get the classes
        Set<Class<?>> entityClasses = scanner.scanEntities();

        // generate models in the db out of this
        EntityGenerator.generateTables(entityClasses);

        Course course = new Course("course 1", "a course after my own heart");

        SessionManagerFactory emf = new SessionManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
