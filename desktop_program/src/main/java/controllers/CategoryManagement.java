package controllers;

import hibernate.CategoryHibernateControl;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CategoryManagement implements Initializable {

    @FXML
    public Button deleteSubcatBtn;
    @FXML
    public Button exitCatBtn;
    @FXML
    public Button renameSubcatBtn;
    @FXML
    public Button mngSubcatBtn;
    @FXML
    public Button mngCatBalBtn;
    @FXML
    public Button mngCatUserBtn;
    @FXML
    public MenuItem saveBtn;
    @FXML
    public TextField subcatRenameField;
    @FXML
    public TextField newSubcatField;
    @FXML
    public Button newSubcatBtn;
    @FXML
    public ListView subcategoryList;
    @FXML
    public Button goToSubcategory;
    @FXML
    public Button previousCatBtn;

    private FinanceSystem financeSystem;
    private Category category;
    private Company company;
    private Person person;
    private Category parentCategory;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(entityManagerFactory);

    public void setFinanceSystem(FinanceSystem financeSystem) {
        this.financeSystem = financeSystem;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void setPerson(Person person) {
        this.person = person;
        fillCategoryListWithData();
    }

    private void fillCategoryListWithData() {
        subcategoryList.getItems().clear();

        if (category.getSubcats().size() != 0) {
            category.getSubcats().forEach(subcategory -> subcategoryList.getItems().add(subcategory.getName() + ": " + subcategory.getCategoryId()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateData() {
        setFinanceSystem(financeSystemHibernateControl.findFinanceSystem(financeSystem.getName()));
        setCategory(categoryHibernateControl.findCategory(category.getCategoryId()));
        if (company != null) setCompany(companyHibernateControl.findCompany(company.getLoginName()));
        if (person != null) setPerson(personHibernateControl.findPerson(person.getLoginName()));
    }

    public void loadExpenseIncomeManagementForm(ActionEvent actionEvent) throws IOException {
        //updateData();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ExpenseIncomeManagement.fxml"));
        Parent root = loader.load();

        ExpenseIncomeManagement expenseIncomeManagement = loader.getController();
        expenseIncomeManagement.setFinanceSystem(financeSystem);
        expenseIncomeManagement.setCategory(category);
        expenseIncomeManagement.setParentCat(category);
        expenseIncomeManagement.setCompany(company);
        expenseIncomeManagement.setPerson(person);

        Stage stage = (Stage) mngCatBalBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void goBackToMainWindow(ActionEvent actionEvent) throws IOException {
        updateData();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/MainAccountingWindow.fxml"));
        Parent root = loader.load();

        MainAccountingWindow mainAccountingWindow = loader.getController();
        mainAccountingWindow.setFinanceSystem(financeSystem);
        mainAccountingWindow.setCompany(company);
        mainAccountingWindow.setPerson(person);

        Stage stage = (Stage) exitCatBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean checkOwnerValidation() {
        if (person != null) {
            if (category.getOwnerName().equals(person.getLoginName())) {
                return true;
            }
        } else if (company != null) {
            if (category.getOwnerName().equals(company.getLoginName())) {
                return true;
            }
        }
        return false;
    }

    public void loadUserManagement(ActionEvent actionEvent) throws IOException {

        if (checkOwnerValidation()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/UserManagement.fxml"));
            Parent root = loader.load();

            UserManagement userManagement = loader.getController();
            userManagement.setFinanceSystem(financeSystem);
            userManagement.setCategory(category);
            userManagement.setCompany(company);
            userManagement.setPerson(person);

            Stage stage = (Stage) mngCatUserBtn.getScene().getWindow();

            stage.setTitle("Finance management system");
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Action request rejected");
            alert.setContentText("You have no permissions to edit this.");

            alert.showAndWait();
        }
    }

    public void createNewSubcategory(ActionEvent actionEvent) {
        Category cate = new Category(newSubcatField.getText(), null, null, null, null, null, null, category, null);
        categoryHibernateControl.create(cate);
        updateData();
        fillCategoryListWithData();
    }

    public void renameSubcategory(ActionEvent actionEvent) {
        String[] subcatData = subcategoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category subcat = category.getSubcats().stream().filter(s -> s.getName().equals(subcatData[0])).findFirst().orElse(null);

        subcat.setName(subcatRenameField.getText());
        categoryHibernateControl.edit(subcat);
        updateData();
        fillCategoryListWithData();
    }

    public void deleteSubcategory(ActionEvent actionEvent) throws Exception {
        String[] subcatData = subcategoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category subcat = category.getSubcats().stream().filter(s -> s.getName().equals(subcatData[0])).findFirst().orElse(null);

        categoryHibernateControl.removeSubcatFromCategory(category, subcat);

        updateData();
        fillCategoryListWithData();
    }

    public void goToSelectedCategory(ActionEvent actionEvent) throws IOException {
        //updateData();
        String[] subcatData = subcategoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category subcat = category.getSubcats().stream().filter(s -> s.getName().equals(subcatData[0])).findFirst().orElse(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CategoryManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceSystem(financeSystem);
        categoryManagement.setCategory(subcat);
        categoryManagement.setParentCategory(category);
        categoryManagement.setCompany(company);
        categoryManagement.setPerson(person);

        Stage stage = (Stage) goToSubcategory.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadExpenseIncomeManagementFormSubcat(ActionEvent actionEvent) throws IOException {
        //updateData();
        String[] subcatData = subcategoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category subcat = category.getSubcats().stream().filter(s -> s.getName().equals(subcatData[0])).findFirst().orElse(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ExpenseIncomeManagement.fxml"));
        Parent root = loader.load();

        ExpenseIncomeManagement expenseIncomeManagement = loader.getController();
        expenseIncomeManagement.setFinanceSystem(financeSystem);
        expenseIncomeManagement.setCategory(subcat);
        expenseIncomeManagement.setParentCat(category);
        expenseIncomeManagement.setCompany(company);
        expenseIncomeManagement.setPerson(person);

        Stage stage = (Stage) mngCatBalBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void backToPreviousCategoryMng(ActionEvent actionEvent) throws IOException {
        updateData();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CategoryManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceSystem(financeSystem);
        categoryManagement.setCategory(category.getParentCat());
        categoryManagement.setCompany(company);
        categoryManagement.setPerson(person);

        Stage stage = (Stage) previousCatBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }
}