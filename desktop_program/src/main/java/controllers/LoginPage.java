package controllers;

import hibernate.CompanyHibernateControl;
import hibernate.PersonHibernateControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Company;
import model.FinanceSystem;
import model.Person;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

    @FXML
    public Button cSignUpBtn;
    @FXML
    public Button cSignInBtn;
    @FXML
    public TextField cSignInLogin;
    @FXML
    public TextField cSignInPsw;
    @FXML
    public Button pSignUpBtn;


    private FinanceSystem financeSystem;
    private boolean isPerson, isCompany;
    private Company company = null;
    private Person person = null;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);

    public void setFinanceSystem(FinanceSystem financeSystem) {
        this.financeSystem = financeSystem;
    }

    public void setPerson(boolean person) {
        this.isPerson = person;
    }

    public void setCompany(boolean company) {
        this.isCompany = company;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void validateUser(ActionEvent actionEvent) throws IOException {
        if (isCompany) {
            Company comp = null;
            if (companyHibernateControl.findCompany(cSignInLogin.getText()) != null) {
                comp = companyHibernateControl.findCompany(cSignInLogin.getText());

                if (!comp.getPsw().equals(cSignInPsw.getText())) {
                    comp = null;
                }
            }
            if (comp != null) {
                company = comp;
                loadMainWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("No such company");

                alert.showAndWait();
            }
        } else if (isPerson) {
            Person per = null;
            if (personHibernateControl.findPerson(cSignInLogin.getText()) != null) {
                per = personHibernateControl.findPerson(cSignInLogin.getText());

                if (!per.getPsw().equals(cSignInPsw.getText())) {
                    per = null;
                }
            }

            if (per != null) {
                person = per;
                loadMainWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("No such person");

                alert.showAndWait();
            }
        } else {
            System.out.println("something is wrong");
        }
    }

    private void loadMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/MainAccountingWindow.fxml"));
        Parent root = loader.load();

        MainAccountingWindow mainAccountingWindow = loader.getController();
        mainAccountingWindow.setFinanceSystem(financeSystem);
        mainAccountingWindow.setCompany(company);
        mainAccountingWindow.setPerson(person);

        Stage stage = (Stage) cSignInBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void createCompany(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CompanyCreation.fxml"));
        Parent root = loader.load();

        CompanyCreation companyCreation = loader.getController();
        companyCreation.setFinanceSystem(financeSystem, isPerson, isCompany);

        Stage stage = (Stage) cSignInBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void createPerson(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/PersonCreation.fxml"));
        Parent root = loader.load();

        PersonCreation personCreation = loader.getController();
        personCreation.setFinanceSystem(financeSystem, isPerson, isCompany);

        Stage stage = (Stage) pSignUpBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
