package controllers;

import hibernate.CategoryHibernateControl;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import hibernate.ExpenseHibernateControl;
import hibernate.IncomeHibernateControl;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class ExpenseIncomeManagement implements Initializable {
    @FXML
    public ListView incomeList;
    @FXML
    public ListView expenseList;
    @FXML
    public Button rditIncomeBtn;
    @FXML
    public Button editExpenseBtn;
    @FXML
    public MenuItem newIncomeBtn;
    @FXML
    public MenuItem newExpenseBtn;
    @FXML
    public MenuItem saveFileBtn;
    @FXML
    public Button deleteIncomeBtn;
    @FXML
    public Button deleteExpenseBtn;
    @FXML
    public Button goBackBtn;
    @FXML
    public TextField incomeDescrBtn;
    @FXML
    public TextField incomeAmountBtn;
    @FXML
    public TextField expenseDescrBtn;
    @FXML
    public TextField expenseAmountBtn;

    private FinanceSystem financeSystem;
    private Category category;
    private Category parentCat;
    private Company company;
    private Person person;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(entityManagerFactory);
    ExpenseHibernateControl expenseHibernateControl = new ExpenseHibernateControl(entityManagerFactory);
    IncomeHibernateControl incomeHibernateControl = new IncomeHibernateControl(entityManagerFactory);

    public void setFinanceSystem(FinanceSystem financeSystem) {
        this.financeSystem = financeSystem;
    }

    public void setCategory(Category category) {
        this.category = category;
        fillIncomeListWithData();
        fillExpenseListWIthData();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setParentCat(Category parentCat) {
        this.parentCat = parentCat;
    }

    private void fillExpenseListWIthData() {
        expenseList.getItems().clear();

        if (category.getExpense().size() != 0) {
            for (Expense e : category.getExpense()) {
                expenseList.getItems().add(e.getDescription() + ": " + e.getAmount());
            }
        }
    }

    private void fillIncomeListWithData() {
        incomeList.getItems().clear();

        if (category.getIncome().size() != 0) {
            for (Income i : category.getIncome()) {
                incomeList.getItems().add(i.getDescription() + ": " + i.getAmount());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateData() {
        setFinanceSystem(financeSystemHibernateControl.findFinanceSystem(financeSystem.getName()));
        setParentCat(categoryHibernateControl.findCategory(parentCat.getCategoryId()));
        setCategory(categoryHibernateControl.findCategory(category.getCategoryId()));
        if (company != null) setCompany(companyHibernateControl.findCompany(company.getLoginName()));
        if (person != null) setPerson(personHibernateControl.findPerson(person.getLoginName()));
    }

    public void editIncomeInfo(ActionEvent actionEvent) throws IOException {
        String[] incomeData = incomeList.getSelectionModel().getSelectedItem().toString().split(": ");
        Income income = category.getIncome().stream().filter(i -> i.getDescription().equals(incomeData[0])).findFirst().orElse(null);

        if (incomeAmountBtn.getText().equals("") && incomeDescrBtn.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Incorrect data");
            alert.setContentText("At least one field must be filled.");
            alert.showAndWait();
        } else {
            if (!incomeAmountBtn.getText().equals("")) {
                income.setAmount(Integer.parseInt(incomeAmountBtn.getText()));
            }
            if (!incomeDescrBtn.getText().equals("")) {
                income.setDescription(incomeDescrBtn.getText());
            }
            incomeHibernateControl.edit(income);
            updateData();
            fillIncomeListWithData();
        }
    }

    public void editExpenseInfo(ActionEvent actionEvent) throws IOException {
        String[] expenseData = expenseList.getSelectionModel().getSelectedItem().toString().split(": ");
        Expense expense = category.getExpense().stream().filter(e -> e.getDescription().equals(expenseData[0])).findFirst().orElse(null);

        if (expenseAmountBtn.getText().equals("") && expenseDescrBtn.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Incorrect data");
            alert.setContentText("At least one field must be filled.");
            alert.showAndWait();
        } else {
            if (!expenseAmountBtn.getText().equals("")) {
                expense.setAmount(Integer.parseInt(expenseAmountBtn.getText()));
            }
            if (!expenseDescrBtn.getText().equals("")) {
                expense.setDescription(expenseDescrBtn.getText());
            }
            expenseHibernateControl.edit(expense);
            updateData();
            fillExpenseListWIthData();
        }

    }

    public void createNewIncome(ActionEvent actionEvent) throws Exception {
        TextInputDialog name = new TextInputDialog("");
        name.setHeaderText("Creating new income");
        name.setContentText("Enter income name:");

        Optional<String> resultName = name.showAndWait();

        String temp;

        if (resultName.isPresent()) {
            temp = resultName.get();
            if (temp.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Income creation failed");
                alert.setContentText("You need to fill all fields.");

                alert.showAndWait();
            } else {
                TextInputDialog amount = new TextInputDialog("");
                amount.setHeaderText("Creating new income");
                amount.setContentText("Enter income amount:");

                Optional<String> resultAmount = amount.showAndWait();
                if (resultAmount.isPresent()) {
                    if (resultAmount.get().equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Expense creation failed");
                        alert.setContentText("You need to fill all fields.");

                        alert.showAndWait();
                    } else {
                        Income inc = new Income(Integer.parseInt(resultAmount.get()), resultName.get(), category);
                        incomeHibernateControl.create(inc);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("You have created new income.");
                        alert.setContentText("Income " + temp + " was created");
                        alert.showAndWait();

                        updateData();
                        fillIncomeListWithData();
                    }
                }
            }
        }
    }

    public void createNewExpense(ActionEvent actionEvent) throws Exception {

        TextInputDialog name = new TextInputDialog("");
        name.setHeaderText("Creating new expense");
        name.setContentText("Enter expense name:");

        Optional<String> resultName = name.showAndWait();

        String temp;

        if (resultName.isPresent()) {
            temp = resultName.get();
            if (temp.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Expense creation failed");
                alert.setContentText("You need to fill all fields.");

                alert.showAndWait();
            } else {

                TextInputDialog amount = new TextInputDialog("");
                amount.setHeaderText("Creating new expense");
                amount.setContentText("Enter expense amount:");

                Optional<String> resultAmount = amount.showAndWait();
                if (resultAmount.isPresent()) {
                    if (resultAmount.get().equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Expense creation failed");
                        alert.setContentText("You need to fill all fields.");

                        alert.showAndWait();
                    } else {
                        Expense exp = new Expense(Integer.parseInt(resultAmount.get()), resultName.get(), category);
                        expenseHibernateControl.create(exp);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("You have created new expense.");
                        alert.setContentText("Expense " + temp + " was created");
                        alert.showAndWait();

                        updateData();
                        fillExpenseListWIthData();
                    }
                }
            }
        }

    }

    public void deleteIncome(ActionEvent actionEvent) throws Exception {
        String[] incomeData = incomeList.getSelectionModel().getSelectedItem().toString().split(": ");
        Income income = category.getIncome().stream().filter(i -> i.getDescription().equals(incomeData[0])).findFirst().orElse(null);
        String temp = incomeData[0];

        //TODO galima ir palikt
        //incomeHibernateControl.remove(income.getId());
        categoryHibernateControl.removeIncomeFromCategory(category, income);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("You have removed income.");
        alert.setContentText("Income " + temp + " was removed.");
        alert.showAndWait();

        updateData();
        fillIncomeListWithData();
    }

    public void deleteExpense(ActionEvent actionEvent) throws Exception {
        String[] expenseData = expenseList.getSelectionModel().getSelectedItem().toString().split(": ");
        Expense expense = category.getExpense().stream().filter(e -> e.getDescription().equals(expenseData[0])).findFirst().orElse(null);
        String temp = expenseData[0];

        //TODO galima ir palikt
        //expenseHibernateControl.remove(expense.getId());
        categoryHibernateControl.removeExpenseFromCategory(category, expense);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("You have removed expense.");
        alert.setContentText("Expense " + temp + " was removed.");
        alert.showAndWait();

        updateData();
        fillExpenseListWIthData();
    }

    public void refreshBalanceWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ExpenseIncomeManagement.fxml"));
        Parent root = loader.load();

        ExpenseIncomeManagement expenseIncomeManagement = loader.getController();
        expenseIncomeManagement.setFinanceSystem(financeSystem);
        expenseIncomeManagement.setCategory(category);
        expenseIncomeManagement.setCompany(company);
        expenseIncomeManagement.setPerson(person);

        Stage stage = (Stage) deleteIncomeBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void backToPreviousWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CategoryManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceSystem(financeSystem);
        categoryManagement.setCategory(parentCat);
        categoryManagement.setCompany(company);
        categoryManagement.setPerson(person);

        Stage stage = (Stage) goBackBtn.getScene().getWindow();

        stage.setTitle("Finance management system");
        stage.setScene(new Scene(root));
        stage.show();
    }
}