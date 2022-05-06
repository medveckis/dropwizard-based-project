package io.medveckis.client.response;

public class BookResponse {
    private Integer id;
    private Double bookFee;
    private String name;
    private Integer quantity;

    public BookResponse() {
    }

    public BookResponse(Integer id, Double bookFee) {
        this.id = id;
        this.bookFee = bookFee;
    }

    public BookResponse(Integer id, Double bookFee, String name, Integer quantity) {
        this.id = id;
        this.bookFee = bookFee;
        this.name = name;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBookFee() {
        return bookFee;
    }

    public void setBookFee(Double bookFee) {
        this.bookFee = bookFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}