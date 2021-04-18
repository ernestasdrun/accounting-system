package controllers;

import hibernate.CategoryHibernateControl;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainAccountingWindow implements Initializable {

    @FXML
    public Button manageCatBtn;
    @FXML
    public Button delCatBtn;
    @FXML
    public Button viewCatBtn;
    @FXML
    public Button renCatBtn;
    @FXML
    public ListView categoryList;
    @FXML
    public MenuItem saveFile;
    @FXML
    public MenuItem newCatBtn;
    @FXML
    public MenuItem userInfoBtn;

    private FinanceSystem financeSystem;
    private Company company;
    private Person person;
    private Category category;

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

    public void setPerson(Person person) {
        this.person = person;
        fillCategoryListWithData();
    }

    private void fillCategoryListWithData() {
        categoryList.getItems().clear();

        if (person != null) {
            for (Category c : person.getResponsibleCategories()) {
                categoryList.getItems().add(c.getName() + ": " + c.getCategoryId());
            }
        } else if (company != null) {
            for (Category c : company.getResponsibleCategories()) {
                categoryList.getItems().add(c.getName() + ": " + c.getCategoryId());
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateData() {
        setFinanceSystem(financeSystemHibernateControl.findFinanceSystem(financeSystem.getName()));
        if (company != null) setCompany(companyHibernateControl.findCompany(company.getLoginName()));
        if (person != null) setPerson(personHibernateControl.findPerson(person.getLoginName()));
    }

    public void loadCategoryManagementForm(ActionEvent actionEvent) throws IOException {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = financeSystem.getAllCategories().stream().filter(c -> c.getName().equals(categoryData[0])).findFirst().orElse(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CategoryManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceSystem(financeSystem);
        categoryManagement.setCategory(category);
        categoryManagement.setCompany(company);
        categoryManagement.setPerson(person);

        Stage stage = (Stage) manageCatBtn.getScene().getWindow();

        stage.setTitle("Accounting system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showCategoryInfo(ActionEvent actionEvent) {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = financeSystem.getAllCategories().stream().filter(c -> c.getName().equals(categoryData[0])).findFirst().orElse(null);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Categories");
        alert.setHeaderText("Category details");
        alert.setContentText("Category: " + category.getName() + ", owner: " + category.getOwnerName());

        alert.showAndWait();
    }

    public void deleteCategory(ActionEvent actionEvent) throws Exception {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = financeSystem.getAllCategories().stream().filter(c -> c.getName().equals(categoryData[0])).findFirst().orElse(null);
        String temp = categoryData[0];

        if (company != null) {

            if (category.getOwnerName().equals(company.getLoginName())) {

                categoryHibernateControl.clearCategoryUsers(category);
                updateData();
                Category cat = null;
                for (Category c : financeSystem.getAllCategories()) {
                    if (c.getCategoryId() == category.getCategoryId()) {
                        cat = c;
                        break;
                    }
                }
                financeSystemHibernateControl.removeCategoryFromFinanceSystem(financeSystem, cat);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("You have removed category.");
                alert.setContentText("Category " + temp + " was removed.");
                alert.showAndWait();

                updateData();
                fillCategoryListWithData();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Action request rejected");
                alert.setContentText("You have no permissions to edit this.");

                alert.showAndWait();
            }

        } else if (person != null) {

            if (category.getOwnerName().equals(person.getLoginName())) {

                categoryHibernateControl.clearCategoryUsers(category);
                updateData();
                Category cat = null;
                for (Category c : financeSystem.getAllCategories()) {
                    if (c.getCategoryId() == category.getCategoryId()) {
                        cat = c;
                        break;
                    }
                }
                financeSystemHibernateControl.removeCategoryFromFinanceSystem(financeSystem, cat);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("You have removed category.");
                alert.setContentText("Category " + temp + " was removed.");
                alert.showAndWait();

                updateData();
                fillCategoryListWithData();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Action request rejected");
                alert.setContentText("You have no permissions to edit this.");

                alert.showAndWait();
            }

        }

    }

    public void renameCategory(ActionEvent actionEvent) throws IOException {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = financeSystem.getAllCategories().stream().filter(c -> c.getName().equals(categoryData[0])).findFirst().orElse(null);
        String temp = categoryData[0];


        if (company != null) {

            System.out.println(category.getCategoryId());
            if (category.getOwnerName().equals(company.getLoginName())) {
                TextInputDialog dialog = new TextInputDialog(temp);
                dialog.setTitle("Rename " + temp);
                dialog.setHeaderText("Rename this category");
                dialog.setContentText("Please enter new name:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    if (result.get().equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Category creation failed");
                        alert.setContentText("You need to fill all fields.");

                        alert.showAndWait();
                    } else {
                        category.setName(result.get());
                        categoryHibernateControl.edit(category);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("You have renamed category.");
                        alert.setContentText("Category " + temp + " was renamed to " + category.getName());
                        alert.showAndWait();

                        updateData();
                        fillCategoryListWithData();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Action request rejected");
                alert.setContentText("You have no permissions to edit this.");

                alert.showAndWait();
            }

        } else if (person != null) {

            if (category.getOwnerName().equals(person.getLoginName())) {
                TextInputDialog dialog = new TextInputDialog(temp);
                dialog.setTitle("Rename " + temp);
                dialog.setHeaderText("Rename this category");
                dialog.setContentText("Please enter new name:");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    if (result.get().equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Category creation failed");
                        alert.setContentText("You need to fill all fields.");

                        alert.showAndWait();
                    } else {
                        category.setName(result.get());
                        categoryHibernateControl.edit(category);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("You have renamed category.");
                        alert.setContentText("Category " + temp + " was renamed to " + category.getName());
                        alert.showAndWait();

                        updateData();
                        fillCategoryListWithData();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Action request rejected");
                alert.setContentText("You have no permissions to edit this.");

                alert.showAndWait();
            }

        }

    }

    public void createNewCategory(ActionEvent actionEvent) throws Exception {
        String temp;

        TextInputDialog name = new TextInputDialog();
        name.setTitle("Create new category");
        name.setHeaderText("Please create new category");
        name.setContentText("Please enter name for your category:");

        Optional<String> result = name.showAndWait();
        if (result.isPresent()) {
            temp = result.get();
            if (temp.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Category creation failed");
                alert.setContentText("You need to fill all fields.");

                alert.showAndWait();
            } else {
                if (company != null) {
                    Category cate = new Category(result.get(), company.getLoginName(), null, null, null, null, null, null, financeSystem);
                    categoryHibernateControl.create(cate);
                    cate = categoryHibernateControl.findCategory(cate.getName());
                    companyHibernateControl.addCategoryToCompany(company.getCompanyId(), cate.getCategoryId());
                    updateData();
                } else if (person != null) {
                    Category cate = new Category(result.get(), person.getLoginName(), null, null, null, null, null, null, financeSystem);
                    categoryHibernateControl.create(cate);
                    cate = categoryHibernateControl.findCategory(cate.getName());
                    personHibernateControl.addCategoryToPerson(person.getPersonId(), cate.getCategoryId());
                    updateData();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("You have created new category.");
                alert.setContentText("Category " + temp + " was created");
                alert.showAndWait();

                fillCategoryListWithData();
            }
        }
    }

}