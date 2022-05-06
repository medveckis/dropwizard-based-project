package io.medveckis.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class BookRecord extends Item {
    private Integer userId;
    private Integer  bookId;
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    private Double fee;
    @Type(type="yes_no")
    private Boolean completed = false;

    public BookRecord() {
    }

    public BookRecord(Integer userId, Integer bookId, Date expirationDate, Double fee, Boolean completed) {
        this.userId = userId;
        this.bookId = bookId;
        this.expirationDate = expirationDate;
        this.fee = fee;
        this.completed = completed;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
