package com.hibernate.xmlbased.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.hibernate.xmlbased.config.SessionConfig;
import com.hibernate.xmlbased.model.Department;
import com.hibernate.xmlbased.model.Developer;

public class DepartmentDAO {

    private final SessionConfig sc;

    public DepartmentDAO() {
        sc = SessionConfig.getInstanceOfSeccionFactory();
    }

    public List<Department> getDepartments() {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Department> deps = session.createQuery("FROM Department", Department.class).getResultList();
        transaction.commit();
        session.close();
        return deps;
    }

    // решает проблему n+1
    public Set<Department> getDepartmentWithWorkers() {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Set<Department> departments = new HashSet<>(session.createQuery("FROM Department d LEFT JOIN FETCH d.developers", Department.class).getResultList());
        transaction.commit();
        session.close();
        return departments;
    }

    public List<Developer> getDevelopersByDepartment(Department department) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Department dep = session.get(Department.class, department.getDepartmentId());
        Hibernate.initialize(dep.getDevelopers());
        List<Developer> devs = dep.getDevelopers();
        transaction.commit();
        session.close();
        return devs;
    }

    public Department findDepartmentByID(String departmentID) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Department dep = session.get(Department.class, departmentID);
        transaction.commit();
        session.close();
        return dep;
    }

    public Department addDepartament(Department dep) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(dep);
            transaction.commit();
            return dep;
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            // можно логировать ex
            return null;
        } finally {
            if (session.isOpen()) session.close();
        }
    }

    public void deleteDepartment(String ID) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Department dep = session.get(Department.class, ID);
        if (dep != null) {
            session.remove(dep);
        }
        transaction.commit();
        session.close();
    }
}
