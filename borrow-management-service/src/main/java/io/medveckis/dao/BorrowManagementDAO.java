package io.medveckis.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.medveckis.model.BookRecord;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class BorrowManagementDAO extends AbstractDAO<BookRecord> {

    public BorrowManagementDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<BookRecord> findAllRecordsByUserId(Integer userId) {
        Query<BookRecord> query = currentSession().createQuery("SELECT br FROM BookRecord br WHERE br.userId=:id ORDER BY br.id");
        query.setParameter("id", userId);
        return query.list();
    }

    public Integer save(BookRecord bookRecord) {
        return persist(bookRecord).getId();
    }

}
