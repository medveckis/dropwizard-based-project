package io.medveckis;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.medveckis.dao.AuthorDAO;
import io.medveckis.dao.BookDAO;
import io.medveckis.dao.CategoryDAO;
import io.medveckis.health.DatabaseHealthCheck;
import io.medveckis.model.Author;
import io.medveckis.model.Book;
import io.medveckis.model.Category;
import io.medveckis.model.Item;
import io.medveckis.web.resource.BookResource;
import io.medveckis.web.resource.CategoryResource;

public class BookServiceApplication extends Application<BookServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new BookServiceApplication().run(args);
    }

    private final HibernateBundle<BookServiceConfiguration> hibernateBundle =
            new HibernateBundle<>(Author.class, Book.class, Category.class, Item.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(BookServiceConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    private final DiscoveryBundle<BookServiceConfiguration> discoveryBundle = new DiscoveryBundle<>() {

        @Override
        public DiscoveryFactory getDiscoveryFactory(BookServiceConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<BookServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(BookServiceConfiguration bookServiceConfiguration, Environment environment) throws Exception {
        BookDAO bookDAO = new BookDAO(hibernateBundle.getSessionFactory());
        AuthorDAO authorDAO = new AuthorDAO(hibernateBundle.getSessionFactory());
        CategoryDAO categoryDAO = new CategoryDAO(hibernateBundle.getSessionFactory());
        DatabaseHealthCheck databaseHealthCheck = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(DatabaseHealthCheck.class, new Class[]{CategoryDAO.class, AuthorDAO.class, BookDAO.class},
                        new Object[] {categoryDAO, authorDAO, bookDAO});
        environment.healthChecks().register("db", databaseHealthCheck);
        environment.jersey().register(new BookResource(bookDAO));
        environment.jersey().register(new CategoryResource(categoryDAO));
        environment.healthChecks().getHealthCheck("db").execute();
    }
}
