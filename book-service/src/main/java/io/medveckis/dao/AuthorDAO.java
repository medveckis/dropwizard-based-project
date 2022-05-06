package io.medveckis.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.medveckis.model.Author;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class AuthorDAO extends AbstractDAO<Author> {

    public AuthorDAO(SessionFactory sessionFactory) {
        super(sessionFactory);

    }

    public int save(Author author) {
        return persist(author).getId();
    }

    public List<Author> findAll() {
        CriteriaQuery<Author> criteriaQuery = criteriaQuery();
        criteriaQuery.from(getEntityClass());
        return currentSession().createQuery(criteriaQuery).getResultList();
    }
}
