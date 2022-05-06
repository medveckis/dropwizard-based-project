package io.medveckis.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.medveckis.model.Category;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CategoryDAO extends AbstractDAO<Category> {

    public CategoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public int save(Category category) {
        return persist(category).getId();
    }

    public List<Category> findAll() {
        CriteriaQuery<Category> criteriaQuery = criteriaQuery();
        criteriaQuery.from(getEntityClass());
        return currentSession().createQuery(criteriaQuery).getResultList();
    }
}
