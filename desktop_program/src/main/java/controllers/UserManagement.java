package controllers;

import hibernate.CategoryHibernateControl;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import model.Category;
import model.Company;
import model.FinanceSystem;
import model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagement implements Initializable {

    @FXML
    public TextField personField;
    @FXML
    public Button addPersonBtn;
    @FXML
    public Button delPersonBtn;
    @FXML
    public Button backBtn;
    @FXML
    public Button addCompanyBtn;
    @FXML
    public TextField companyField;
    @FXML
    public Button delCompanyBtn;
    @FXML
    public ListView personList;
    @FXML
    public ListView companyList;

    private FinanceSystem financeSystem;
    private Category category;
    private Person person;
    private Company company;

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

    public void setPerson(Person person) {
        this.person = person;
        fillCompanyListWithData();
        fillPersonListWithData();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void fillCompanyListWithData() {
        companyList.getItems().clear();

        List<Company> comp = categoryHibernateControl.getValidCompanies(category);
        if (comp != null)
            for (Company c : comp) {
                if (!category.getOwnerName().equals(c.getLoginName()))
                    companyList.getItems().add(c.getLoginName() + ": " + c.getName() + ", " + c.getEmail() + ", " + c.getPhoneNum());
            }

    }

    public void fillPersonListWithData() {
        personList.getItems().clear();

        List<Person> per = categoryHibernateControl.getValidPeople(category);
        if (per != null)
            for (Person p : per) {
                if (!category.getOwnerName().equals(p.getLoginName()))
                    personList.getItems().add(p.getLoginName() + ": " + p.getName() + ", " + p.getEmail() + ", " + p.getPhoneNum());
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

    public void addNewPerson(ActionEvent actionEvent) throws Exception {
        int count = 0;
        for (Person p : financeSystem.getAllPeople()) {
            if (p.getLoginName().equals(personField.getText())) {

                List<Person> per = categoryHibernateControl.getValidPeople(category);

                if (per != null)
                    for (Person pe : per) {
                        if (p.getLoginName().equals(pe.getLoginName())) {
                            count++;
                            if (count == 1) break;
                        }
                    }
                if (count == 0) {
                    personHibernateControl.addCategoryToPerson(p.getPersonId(), category.getCategoryId());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Person added successfully");

                    alert.showAndWait();

                    count = 2;

                    updateData();
                    fillPersonListWithData();
                    break;
                }
            }
        }
        if (count == 0 || count == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("This user does not exist or is already added");

            alert.showAndWait();
        }
    }


    public void addNewCompany(ActionEvent actionEvent) throws Exception {
        int count = 0;
        for (Company c : financeSystem.getAllCompanies()) {
            if (c.getLoginName().equals(companyField.getText())) {

                List<Company> comp = categoryHibernateControl.getValidCompanies(category);

                if (comp != null)
                    for (Company co : comp) {
                        if (c.getName().equals(co.getName())) {
                            count++;
                            if (count == 1) break;
                        }
                    }
                if (count == 0) {
                    companyHibernateControl.addCategoryToCompany(c.getCompanyId(), category.getCategoryId());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Company added successfully");

                    alert.showAndWait();

                    count = 2;

                    updateData();
                    fillCompanyListWithData();
                    break;
                }

            }
        }
        if (count == 0 || count == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("This user does not exist or is already added");

            alert.showAndWait();
        }
    }


    public void removePerson(ActionEvent actionEvent) throws Exception {
        String[] personData = personList.getSelectionModel().getSelectedItem().toString().split(": ");
        Person per = financeSystem.getAllPeople().stream().filter(p -> p.getLoginName().equals(personData[0])).findFirst().orElse(null);

        personHibernateControl.removeCategoryFromPerson(per, category);

        updateData();
        fillPersonListWithData();
    }

    public void removeCompany(ActionEvent actionEvent) throws Exception {
        String[] companyData = companyList.getSelectionModel().getSelectedItem().toString().split(": ");
        Company com = financeSystem.getAllCompanies().stream().filter(c -> c.getLoginName().equals(companyData[0])).findFirst().orElse(null);

        companyHibernateControl.removeCategoryFromCompany(com, category);

        updateData();
        fillCompanyListWithData();
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        updateData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CategoryManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceSystem(financeSystem);
        categoryManagement.setCategory(category);
        categoryManagement.setCompany(company);
        categoryManagement.setPerson(person);

        Stage stage = (Stage) backBtn.getScene().getWindow();

        stage.setTitle("Accounting system");
        stage.setScene(new Scene(root));
        stage.show();
    }

}