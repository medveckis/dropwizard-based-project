package io.medveckis.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {
    @JsonProperty("id")
    private Integer userId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private Integer loyaltyLevel;
    private String role;

    public UserResponse() {
    }

    public UserResponse(Integer userId, Integer loyaltyLevel) {
        this.userId = userId;
        this.loyaltyLevel = loyaltyLevel;
    }

    public UserResponse(Integer userId, String firstName, String lastName, Integer age, String email, Integer loyaltyLevel, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.loyaltyLevel = loyaltyLevel;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(Integer loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}