package controllers;

import hibernate.FinanceSystemHibernateControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.FinanceSystem;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

public class StartApp extends Application {

    public static FinanceSystem financeSystem = new FinanceSystem();

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/LoginPage.fxml"));
        Parent root = loader.load();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
        FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);

        if (financeSystemHibernateControl.getFinanceSystemCount() == 0) {
            financeSystemHibernateControl.create(new FinanceSystem("Kompanija", "8888", "@mail", null, null, null));
        } else {
            financeSystem = financeSystemHibernateControl.findFinanceSystem("Kompanija");
        }



        LoginPage loginPage = loader.getController();

        ButtonType buttonTypeOne = new ButtonType("Company");
        ButtonType buttonTypeTwo = new ButtonType("Person");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("User form");
        alert.setContentText("Choose user type");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            loginPage.setCompany(true);
        } else {
            loginPage.setPerson(true);
        }
        loginPage.setFinanceSystem(financeSystem);

        primaryStage.setTitle("Finance management system");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
