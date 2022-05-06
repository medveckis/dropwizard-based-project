package io.medveckis.web.resource;

import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.dao.BookDAO;
import io.medveckis.web.dto.BookData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path(value = "/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    private final BookDAO bookDAO;

    public BookResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @UnitOfWork
    public List<BookData> getAllBooksByCategoriesAndType(@QueryParam(value = "categories") String categories) {
        return bookDAO.findAllBooksByCategories(Arrays.stream(categories.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
                )
                .stream()
                .map(book -> new BookData(book.getId(), book.getName(), book.getQuantity(), book.getBookFee()))
                .collect(Collectors.toList());
    }

    @GET
    @Path(value = "/{bookId}")
    @UnitOfWork
    public BookData getBookById(@PathParam(value = "bookId") Integer bookId) {
        return bookDAO.findBookById(bookId)
                .map(book -> new BookData(book.getId(), book.getName(), book.getQuantity(), book.getBookFee()))
                .orElseThrow();
    }

}
