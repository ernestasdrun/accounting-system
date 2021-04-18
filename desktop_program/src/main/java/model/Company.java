package model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Company extends User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int companyId;
    private String phoneNum;
    private String email;
    private String address;
    private boolean isValid;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "validatedCompanies")
    @OrderBy("categoryId ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> responsibleCategories;
    @ManyToOne
    private FinanceSystem financeSystem;


    public Company() {

    }

    public Company(String name, String psw, String loginName, String phoneNum, String email, String address, boolean isValid, List<Category> responsibleCategories, FinanceSystem financeSystem) {
        super(name, psw, loginName);
        this.phoneNum = phoneNum;
        this.email = email;
        this.address = address;
        this.isValid = isValid;
        this.responsibleCategories = responsibleCategories;
        this.financeSystem = financeSystem;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
