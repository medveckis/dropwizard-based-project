package io.medveckis.web.form;

public class BookRecordForm {
    private Integer bookId;
    private Integer userId;

    public BookRecordForm() {
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
