package controllers;

import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.FinanceSystem;
import model.Person;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonCreation implements Initializable {

    @FXML
    public TextField pSignInLoginFireld;
    @FXML
    public TextField pSignInPhoneFireld;
    @FXML
    public TextField pSignInNameFireld;
    @FXML
    public TextField pSignInEmailFireld;
    @FXML
    public TextField pSignInSurnameFireld;
    @FXML
    public PasswordField pSignInPswFireld;
    @FXML
    public Button pRegisterBtn;
    @FXML
    public Button pSignInBackBtn;

    private boolean isPerson, isCompany;
    private FinanceSystem financeSystem;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);

    public void setFinanceSystem(FinanceSystem financeSystem, boolean isPerson, boolean isCompany) {
        this.financeSystem = financeSystem;
        this.isPerson = isPerson;
        this.isCompany = isCompany;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateData() {
        setFinanceSystem(financeSystemHibernateControl.findFinanceSystem(financeSystem.getName()), isPerson, isCompany);
    }

    public void createNewPerson(ActionEvent actionEvent) throws Exception {
        if (pSignInNameFireld.getText().equals("") ||
                pSignInSurnameFireld.getText().equals("") || pSignInLoginFireld.getText().equals("") ||
                pSignInPswFireld.getText().equals("") || pSignInPhoneFireld.getText().equals("") ||
                pSignInEmailFireld.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please fill in all fields.");

            alert.showAndWait();
        } else {

            Person person = new Person(pSignInNameFireld.getText(),
                    pSignInPswFireld.getText(), pSignInLoginFireld.getText(),
                    /*pSignInLoginFireld.getText().hashCode(),*/ pSignInSurnameFireld.getText(),
                    pSignInPhoneFireld.getText(), pSignInEmailFireld.getText(),
                    true, null, financeSystem);

            personHibernateControl.create(person);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("You have successfully registered.");

            alert.showAndWait();
        }
        updateData();
    }

    public void goBackToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/LoginPage.fxml"));
        Parent root = loader.load();

        LoginPage loginPage = loader.getController();
        loginPage.setFinanceSystem(financeSystem);
        loginPage.setCompany(isCompany);
        loginPage.setPerson(isPerson);

        Stage stage = (Stage) pSignInBackBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
