package controllers;

import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompanyCreation implements Initializable {

    @FXML
    public Button cRegisterBtn;
    @FXML
    public TextField cSignInLoginFireld;
    @FXML
    public TextField cSignInPhoneFireld;
    @FXML
    public TextField cSignInPswFireld;
    @FXML
    public TextField cSignInNameFireld;
    @FXML
    public TextField cSignInEmailFireld;
    @FXML
    public TextField cSignInAddressFireld;
    @FXML
    public Button cSignInBackBtn;

    private boolean isPerson, isCompany;
    private FinanceSystem financeSystem;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);

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

    public void createNewCompany(ActionEvent actionEvent) throws Exception {

        if (cSignInNameFireld.getText().equals("") ||
                cSignInLoginFireld.getText().equals("") || cSignInPswFireld.getText().equals("") ||
                cSignInPhoneFireld.getText().equals("") || cSignInEmailFireld.getText().equals("") ||
                cSignInAddressFireld.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please fill in all fields.");

            alert.showAndWait();
        } else {

            Company company = new Company(cSignInNameFireld.getText(),
                    cSignInPswFireld.getText(), cSignInLoginFireld.getText(),
                    /*cSignInLoginFireld.getText().hashCode(),*/ cSignInPhoneFireld.getText(),
                    cSignInEmailFireld.getText(), cSignInAddressFireld.getText(),
                    true, null, financeSystem);

            companyHibernateControl.create(company);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("You have successfully registered.");

            alert.showAndWait();
        }
        updateData();
    }


    public void goBackToLoginWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/LoginPage.fxml"));
        Parent root = loader.load();

        LoginPage loginPage = loader.getController();
        loginPage.setFinanceSystem(financeSystem);
        loginPage.setCompany(isCompany);
        loginPage.setPerson(isPerson);

        Stage stage = (Stage) cSignInBackBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
