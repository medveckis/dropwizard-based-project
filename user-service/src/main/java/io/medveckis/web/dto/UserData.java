package io.medveckis.web.dto;

import io.medveckis.model.Role;

public class UserData {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private Integer loyaltyLevel;
    private Role role;

    public UserData() {
    }

    public UserData(Integer id, String firstName, String lastName, Integer age, String email, Integer loyaltyLevel, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.loyaltyLevel = loyaltyLevel;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

