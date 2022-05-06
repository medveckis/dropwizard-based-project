package io.medveckis.health;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.dao.AuthorDAO;
import io.medveckis.dao.BookDAO;
import io.medveckis.dao.CategoryDAO;
import io.medveckis.model.Author;
import io.medveckis.model.Book;
import io.medveckis.model.Category;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseHealthCheck extends HealthCheck {

    private final AuthorDAO authorDAO;
    private final CategoryDAO categoryDAO;
    private final BookDAO bookDAO;

    public DatabaseHealthCheck(CategoryDAO categoryDAO, AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.categoryDAO = categoryDAO;
        this.bookDAO = bookDAO;
    }

    @Override
    @UnitOfWork
    protected Result check() throws Exception {
        dataLoaderForCategories();
        dataLoaderForAuthors();
        dataLoaderForBooks(authorDAO.findAll(), categoryDAO.findAll());
        return Result.healthy();
    }


    private void dataLoaderForBooks(List<Author> authors, List<Category> categories) {
        IntStream.range(0, 10).forEach(idx -> bookDAO.save(createBook(idx, authors, categories)));
    }


    private void dataLoaderForAuthors() {
        IntStream.range(0, 5).forEach(idx -> authorDAO.save(createAuthor(idx)));
    }


    private void dataLoaderForCategories() {
        IntStream.range(0, 5).forEach(idx -> categoryDAO.save(createCategory(idx)));
    }

    private Book createBook(int idx, List<Author> authors, List<Category> categories) {
        String name = "name_" + idx;
        int qty = idx + 10;
        double feeCounter = 0;
        Collections.shuffle(authors);
        Collections.shuffle(categories);
        return new Book(name, qty, ++feeCounter, authors.stream().limit(2).collect(Collectors.toList()),
                categories.stream().limit(3).collect(Collectors.toList()));
    }

    private Author createAuthor(int idx) {
        Random rng = new Random();
        String name = "name_" + idx;
        Date bornOn = new Date(rng.nextInt());
        return new Author(name, bornOn);
    }

    private Category createCategory(int idx) {
        return new Category("Category_" + idx);
    }
}
