package model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FinanceSystem implements Serializable {

    @Id
    private String name;
    private String phoneNumber;
    private String emailAddress;

    @OneToMany(mappedBy = "financeSystem", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("PersonId ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Person> allPeople;

    @OneToMany(mappedBy = "financeSystem", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("companyId ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Company> allCompanies;

    @OneToMany(mappedBy = "financeSystem", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("categoryId ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> allCategories;

    public FinanceSystem() {

    }

    public FinanceSystem(String name, String phoneNumber, String emailAddress, List<Person> allPeople, List<Company> allCompanies, List<Category> allCategories) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.allPeople = allPeople;
        this.allCompanies = allCompanies;
        this.allCategories = allCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Person> getAllPeople() {
        return allPeople;
    }

    public void setAllPeople(List<Person> allPeople) {
        this.allPeople = allPeople;
    }

    public List<Company> getAllCompanies() {
        return allCompanies;
    }

    public void setAllCompanies(List<Company> allCompanies) {
        this.allCompanies = allCompanies;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public void addPerson(Person person) {
        this.getAllPeople().add(person);
    }

    public void addCompany(Company company) {
        this.getAllCompanies().add(company);
    }

    public void addCategory(Category category) {
        this.getAllCategories().add(category);
    }
}
