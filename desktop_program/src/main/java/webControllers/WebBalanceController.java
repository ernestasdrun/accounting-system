package webControllers;

import GSONSerializable.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hibernate.CategoryHibernateControl;
import hibernate.ExpenseHibernateControl;
import hibernate.IncomeHibernateControl;
import model.Category;
import model.Expense;
import model.Income;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
public class WebBalanceController {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(entityManagerFactory);
    ExpenseHibernateControl expenseHibernateControl = new ExpenseHibernateControl(entityManagerFactory);
    IncomeHibernateControl incomeHibernateControl = new IncomeHibernateControl(entityManagerFactory);

    @RequestMapping(value = "balance/expListInCat", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllExpenseInCategory(@RequestParam("category") String category) {

        if (category.equals("")) return "No category provided";
        Category cat = categoryHibernateControl.findCategory(category);
        List<Expense> catExp = expenseHibernateControl.getExpenseListInCategory(cat);

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Expense.class, new ExpenseGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(catExp.get(0));

        Type expList = new TypeToken<List<Expense>>() {
        }.getType();
        gson.registerTypeAdapter(expList, new AllExpenseGSONSerializer());
        parser = gson.create();

        return parser.toJson(catExp);
    }


    @RequestMapping(value = "balance/incListInCat", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllIncomeInCategory(@RequestParam("category") String category) {

        if (category.equals("")) return "No category provided";
        Category cat = categoryHibernateControl.findCategory(category);
        List<Income> catInc = incomeHibernateControl.getIncomeListInCategory(cat);

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Income.class, new IncomeGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(catInc.get(0));

        Type incList = new TypeToken<List<Income>>() {
        }.getType();
        gson.registerTypeAdapter(incList, new AllIncomeGSONSerializable());
        parser = gson.create();

        return parser.toJson(catInc);
    }


    @RequestMapping(value = "balance/expInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getExpenseInfoById(@RequestParam("id") String id) {

        if (id.equals("")) return "No expense id provided";

        Expense expense = expenseHibernateControl.findExpense(Integer.parseInt(id));
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Expense.class, new ExpenseGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(expense);
    }


    @RequestMapping(value = "balance/incInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getIncomeInfoById(@RequestParam("id") String id) {

        if (id.equals("")) return "No income id provided";

        Income income = incomeHibernateControl.findIncome(Integer.parseInt(id));
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Income.class, new IncomeGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(income);
    }


    @RequestMapping(value = "balance/updateExpense", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateExpense(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String amount = data.getProperty("amount");
        String id = data.getProperty("id");

        Expense expense = expenseHibernateControl.findExpense(Integer.parseInt(id));

        if (expense != null) {
            expense.setDescription(description);
            expense.setAmount(Integer.parseInt(amount));
            try {
                expenseHibernateControl.edit(expense);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such expense";
    }


    @RequestMapping(value = "balance/updateIncome", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateIncome(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String amount = data.getProperty("amount");
        String id = data.getProperty("id");

        Income income = incomeHibernateControl.findIncome(Integer.parseInt(id));

        if (income != null) {
            income.setDescription(description);
            income.setAmount(Integer.parseInt(amount));
            try {
                incomeHibernateControl.edit(income);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such expense";
    }


    @RequestMapping(value = "balance/createExpense", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createExpense(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String amount = data.getProperty("amount");
        String category = data.getProperty("category");

        Category cat = categoryHibernateControl.findCategory(category);

        if (cat != null) {
            try {
                expenseHibernateControl.create(new Expense(Integer.parseInt(amount), description, cat));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "No such category";
    }


    @RequestMapping(value = "balance/createIncome", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createIncome(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String amount = data.getProperty("amount");
        String category = data.getProperty("category");

        Category cat = categoryHibernateControl.findCategory(category);

        if (cat != null) {
            try {
                incomeHibernateControl.create(new Income(Integer.parseInt(amount), description, cat));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "No such category";
    }

    //TODO neistrina

    @RequestMapping(value = "balance/deleteExpense", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteExpense(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String category = data.getProperty("category");

        Category cat = categoryHibernateControl.findCategory(category);
        Expense exp = expenseHibernateControl.findExpense(description);

        if (exp != null && cat != null) {
            try {
                categoryHibernateControl.removeExpenseFromCategory(cat, exp);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such expense or category";
    }

    //TODO neistrina

    @RequestMapping(value = "balance/deleteIncome", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteIncome(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String description = data.getProperty("description");
        String category = data.getProperty("category");

        Category cat = categoryHibernateControl.findCategory(category);
        Income inc = incomeHibernateControl.findIncome(description);

        if (inc != null && cat != null) {
            try {
                categoryHibernateControl.removeIncomeFromCategory(cat, inc);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such income or category";
    }

}
