package io.medveckis.web.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.medveckis.client.BookServiceClient;
import io.medveckis.client.UserServiceClient;
import io.medveckis.client.response.BookResponse;
import io.medveckis.client.response.UserResponse;
import io.medveckis.dao.BorrowManagementDAO;
import io.medveckis.model.BookRecord;
import io.medveckis.web.dto.BookData;
import io.medveckis.web.dto.BookRecordData;
import io.medveckis.web.dto.UserData;
import io.medveckis.web.form.BookRecordForm;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.stream.Collectors;

@Path(value = "/borrow-management")
@Produces(MediaType.APPLICATION_JSON)
public class BorrowManagementResource {

    private final BorrowManagementDAO borrowManagementDAO;
    private final UserServiceClient userServiceClient;
    private final BookServiceClient bookServiceClient;


    public BorrowManagementResource(BorrowManagementDAO borrowManagementDAO, UserServiceClient userServiceClient, BookServiceClient bookServiceClient) {
        this.borrowManagementDAO = borrowManagementDAO;
        this.userServiceClient = userServiceClient;
        this.bookServiceClient = bookServiceClient;
    }

    @GET
    @Path(value = "/records")
    @UnitOfWork
    public Response getAllRecordsByUser(@QueryParam(value = "userId") Integer userId) {
        return Response.ok(borrowManagementDAO.findAllRecordsByUserId(userId).stream()
                .map(this::convertToData)
                .collect(Collectors.toList())
        ).build();
    }

    @POST
    @Path(value = "/records")
    @UnitOfWork
    public Response createRecord(BookRecordForm bookRecordForm, @Context UriInfo uriInfo) {
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(borrowManagementDAO.save(createRecord(convert(bookRecordForm)))));
        return Response.created(uriBuilder.build()).build();
    }

    private BookRecordData convert(BookRecordForm bookRecordForm) {
        BookRecordData bookRecordData = new BookRecordData();
        UserData userData = new UserData();
        userData.setId(bookRecordForm.getUserId());
        bookRecordData.setUser(userData);
        BookData bookData = new BookData();
        bookData.setId(bookRecordForm.getBookId());
        bookRecordData.setBook(bookData);
        return bookRecordData;
    }


    public BookRecord createRecord(BookRecordData bookRecordData) {
        return convert(bookRecordData,
                bookServiceClient.getBookById(bookRecordData.getBook().getId()).getBookFee(),
                userServiceClient.getUserById(bookRecordData.getUser().getId()).getLoyaltyLevel());
    }

    private BookRecord convert(BookRecordData bookRecordData, Double bookFee, Integer loyaltyLevel) {
        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookId(bookRecordData.getBook().getId());
        bookRecord.setUserId(bookRecordData.getUser().getId());
        bookRecord.setExpirationDate(DateTime.now().plusMonths(3).toDate());
        bookRecord.setFee(loyaltyLevel == 0 ? bookFee : bookFee - (bookFee * 0.1 * loyaltyLevel));
        return bookRecord;
    }

    private BookRecordData convertToData(BookRecord bookRecord) {
        BookRecordData bookRecordData = new BookRecordData();

        UserData userData = new UserData();
        UserResponse userResponse = userServiceClient.getUserById(bookRecord.getUserId());
        userData.setId(userResponse.getUserId());
        userData.setFirstName(userResponse.getFirstName());
        userData.setLastName(userResponse.getLastName());
        userData.setEmail(userResponse.getEmail());
        userData.setAge(userResponse.getAge());

        bookRecordData.setUser(userData);

        BookResponse bookResponse = bookServiceClient.getBookById(bookRecord.getBookId());
        BookData bookData = new BookData();
        bookData.setId(bookResponse.getId());
        bookData.setName(bookResponse.getName());

        bookRecordData.setBook(bookData);

        bookRecordData.setExpirationDate(bookRecord.getExpirationDate());
        bookRecordData.setFee(bookRecord.getFee());
        bookRecordData.setCompleted(bookRecord.getCompleted());

        return bookRecordData;
    }
}
