package io.medveckis.web.dto;

public class BookData {
    private Integer id;
    private String name;
    private Integer quantity;
    private Double bookFee;

    public BookData() {
    }

    public BookData(Integer id, String name, Integer quantity, Double bookFee) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.bookFee = bookFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getBookFee() {
        return bookFee;
    }

    public void setBookFee(Double bookFee) {
        this.bookFee = bookFee;
    }
}
