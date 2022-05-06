package io.medveckis.dao;

import io.dropwizard.hibernate.AbstractDAO;
import io.medveckis.model.Book;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class BookDAO extends AbstractDAO<Book> {

    public BookDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Book> findBookById(Integer bookId) {
        return Optional.ofNullable(get(bookId));
    }

    public int save(Book book) {
        return persist(book).getId();
    }

    public List<Book> findAllBooksByCategories(List<Integer> categoryIds) {
        Query<Book> query = currentSession().createQuery("SELECT DISTINCT b FROM Book b JOIN b.categories c WHERE c.id IN :ids ORDER BY b.id");
        query.setParameter("ids", categoryIds);
        return query.list();
    }

}
