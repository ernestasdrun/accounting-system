package com.example.financingappcurrent.Objects;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int categoryId;
    private String name;
    private String ownerName;

    private List<Income> income;
    private List<Expense> expense;
    private List<Person> validatedPeople;
    private List<Company> validatedCompanies;
    private List<Category> subcats;
    private Category parentCat;
    private FinanceSystem financeSystem;

    public Category() {

    }

    public Category(String name, String ownerName, List<Income> income, List<Expense> expense,
                    List<Person> validatedPeople, List<Company> validatedCompanies, List<Category> subcats, Category parentCat, FinanceSystem financeSystem) {
        this.name = name;
        this.ownerName = ownerName;
        this.income = income;
        this.expense = expense;
        this.validatedPeople = validatedPeople;
        this.validatedCompanies = validatedCompanies;
        this.subcats = subcats;
        this.parentCat = parentCat;
        this.financeSystem = financeSystem;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<Income> getIncome() {
        return income;
    }

    public void setIncome(List<Income> income) {
        this.income = income;
    }

    public List<Expense> getExpense() {
        return expense;
    }

    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }

    public List<Person> getValidatedPeople() {
        if (validatedPeople == null) {
            validatedPeople = new ArrayList<>();
        }
        return validatedPeople;
    }

    public void setValidatedPeople(List<Person> validatedPeople) {
        this.validatedPeople = validatedPeople;
    }

    public List<Company> getValidatedCompanies() {
        if (validatedCompanies == null) {
            validatedCompanies = new ArrayList<>();
        }
        return validatedCompanies;
    }

    public void setValidatedCompanies(List<Company> validatedCompanies) {
        this.validatedCompanies = validatedCompanies;
    }

    public List<Category> getSubcats() {
        return subcats;
    }

    public void setSubcats(List<Category> subcats) {
        this.subcats = subcats;
    }

    public Category getParentCat() {
        return parentCat;
    }

    public void setParentCat(Category parentCat) {
        this.parentCat = parentCat;
    }

    public FinanceSystem getFinanceSystem() {
        return financeSystem;
    }

    public void setFinanceSystem(FinanceSystem financeSystem) {
        this.financeSystem = financeSystem;
    }

    public void addIncome(Income income) {
        this.getIncome().add(income);
    }

    public void addExpense(Expense expense) {
        this.getExpense().add(expense);
    }

    public void addSubcategory(Category category) {
        this.getSubcats().add(category);
    }

    public void addPerson(Person person) {
        this.getValidatedPeople().add(person);
    }

    public void addCompany(Company company) {
        this.getValidatedCompanies().add(company);
    }

    public void removePerson(Person person) {
        this.getValidatedPeople().remove(person);
    }

    public void removeCompany(Company company) {
        this.getValidatedCompanies().remove(company);
    }

    @Override
    public String toString() {
        return  "(ID: " + categoryId + ") Name: " + name + ", owner: " + ownerName;
    }
}
