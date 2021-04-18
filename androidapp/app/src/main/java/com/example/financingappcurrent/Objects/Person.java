package com.example.financingappcurrent.Objects;

import java.util.List;

public class Person extends User {
    private int PersonId;
    private String surname;
    private String phoneNum;
    private String email;
    private boolean isValid;
    private List<Category> responsibleCategories;
    private FinanceSystem financeSystem;


    public Person() {

    }

    public Person(String name, String psw, String loginName, String surname, String phoneNum, String email, boolean isValid, List<Category> responsibleCategories, FinanceSystem financeSystem) {
        super(name, psw, loginName);
        this.surname = surname;
        this.phoneNum = phoneNum;
        this.email = email;
        this.isValid = isValid;
        this.responsibleCategories = responsibleCategories;
        this.financeSystem = financeSystem;
    }

    public int getPersonId() {
        return PersonId;
    }

    public void setPersonId(int personId) {
        PersonId = personId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<Category> getResponsibleCategories() {
        return responsibleCategories;
    }

    public void setResponsibleCategories(List<Category> responsibleCategories) {
        this.responsibleCategories = responsibleCategories;
    }

    public FinanceSystem getFinanceSystem() {
        return financeSystem;
    }

    public void setFinanceSystem(FinanceSystem financeSystem) {
        this.financeSystem = financeSystem;
    }

    public void removeCategory(Category category) {
        this.getResponsibleCategories().remove(category);
    }

    public void addCategory(Category category) {
        this.getResponsibleCategories().add(category);
    }
}
