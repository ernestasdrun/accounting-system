package webControllers;

import GSONSerializable.AllCategoryGSONSerializer;
import GSONSerializable.CategoryGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hibernate.CategoryHibernateControl;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import model.Category;
import model.Company;
import model.FinanceSystem;
import model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class WebCategoryController {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(entityManagerFactory);
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);

    //TODO - prideti ar pasalinti zmones

    @RequestMapping(value = "category/subcatList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllSubcategoriesOfCategory(@RequestParam("parentId") String id) {

        if (id.equals("")) return "No id provided";

        Category category = categoryHibernateControl.findCategory(Integer.parseInt(id));
        List<Category> allCat;

        if (category != null) {
            try {

                allCat = new ArrayList<>(category.getSubcats());

            } catch (Exception e) {
                return "Error";
            }
        } else return "No parent category id provided";

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Category.class, new CategoryGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allCat.get(0));

        Type catList = new TypeToken<List<Category>>() {
        }.getType();
        gson.registerTypeAdapter(catList, new AllCategoryGSONSerializer());
        parser = gson.create();

        return parser.toJson(allCat);
    }


    @RequestMapping(value = "category/companyCatList", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCategoriesOfCompany(@RequestBody String request/*@RequestParam("userId") String id*/) {

        //if (id.equals("")) return "No id provided";
        Gson parse = new Gson();
        Properties data = parse.fromJson(request, Properties.class);
        String id = data.getProperty("userId");


        List<Category> allCat = new ArrayList<>();

        if (id != null) {
            try {
                Company company = companyHibernateControl.findCompany(Integer.parseInt(id));
                allCat.addAll(company.getResponsibleCategories());
            } catch (Exception e) {
                return "Error";
            }
        } else return "No id provided";

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Category.class, new CategoryGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allCat.get(0));

        Type catList = new TypeToken<List<Category>>() {
        }.getType();
        gson.registerTypeAdapter(catList, new AllCategoryGSONSerializer());
        parser = gson.create();

        return parser.toJson(allCat);
    }


    @RequestMapping(value = "category/personCatList", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCategoriesOfPerson(@RequestBody String request) {

        //if (id.equals("")) return "No id provided";
        Gson parse = new Gson();
        Properties data = parse.fromJson(request, Properties.class);
        String id = data.getProperty("userId");

        List<Category> allCat = new ArrayList<>();

        if (id != null) {
            try {
                Person person = personHibernateControl.findPerson(Integer.parseInt(id));
                allCat.addAll(person.getResponsibleCategories());
            } catch (Exception e) {
                return "Error";
            }
        } else return "No id provided";

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Category.class, new CategoryGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allCat.get(0));

        Type catList = new TypeToken<List<Category>>() {
        }.getType();
        gson.registerTypeAdapter(catList, new AllCategoryGSONSerializer());
        parser = gson.create();

        return parser.toJson(allCat);
    }


    @RequestMapping(value = "category/catInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategoryInfoByName(@RequestParam("name") String name) {

        if (name.equals("")) return "No category name provided";

        Category category = categoryHibernateControl.findCategory(name);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Category.class, new CategoryGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(category);
    }


    @RequestMapping(value = "category/updateCategory", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCategory(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String id = data.getProperty("id");

        Category category = categoryHibernateControl.findCategory(Integer.parseInt(id));

        if (category != null) {
            category.setName(name);
            try {
                categoryHibernateControl.edit(category);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such category";
    }


    @RequestMapping(value = "category/createSubcat", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createSubcategory(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String parent = data.getProperty("parentCategory");

        Category cat = categoryHibernateControl.findCategory(parent);
        Category subcat = categoryHibernateControl.findCategory(name);

        if (cat != null && subcat == null) {
            try {
                categoryHibernateControl.create(new Category(name, null, null, null, null, null, null, cat, null));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "Parent category with this name doesn't exist or subcategory name is already taken";
    }


    @RequestMapping(value = "category/createCategory", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String owner = data.getProperty("ownerName");
        String ownerL = data.getProperty("ownerLogin");
        String ownerType = data.getProperty("ownerType"); // p / c
        String system = data.getProperty("system");

        Category category = categoryHibernateControl.findCategory(name);
        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(system);

        if (category == null && financeSystem != null) {
            try {
                if (ownerType.equals("p")) {
                    Person person = personHibernateControl.findPerson(ownerL);
                    Category cate = new Category(name, owner, null, null, null, null, null, null, financeSystem);
                    categoryHibernateControl.create(cate);
                    cate = categoryHibernateControl.findCategory(cate.getName());
                    companyHibernateControl.addCategoryToCompany(person.getPersonId(), cate.getCategoryId());
                } else if (ownerType.equals("c")) {
                    Company company = companyHibernateControl.findCompany(ownerL);
                    Category cate = new Category(name, owner, null, null, null, null, null, null, financeSystem);
                    categoryHibernateControl.create(cate);
                    cate = categoryHibernateControl.findCategory(cate.getName());
                    companyHibernateControl.addCategoryToCompany(company.getCompanyId(), cate.getCategoryId());
                } else return "Owner type must be person or company (p / c)";

            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "Bad system name or category name is already taken";
    }


    @RequestMapping(value = "category/deleteSubcat", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteSubcategory(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");

        Category category = categoryHibernateControl.findCategory(Integer.parseInt(id));

        if (category != null) {
            try {
                categoryHibernateControl.removeSubcatFromCategory(category.getParentCat(), category);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such category";
    }


    @RequestMapping(value = "category/deleteCategory", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCategory(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String system = data.getProperty("system");

        Category category = categoryHibernateControl.findCategory(Integer.parseInt(id));

        if (category != null) {
            try {
                categoryHibernateControl.clearCategoryUsers(category);
                FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(system);
                Category cat = null;
                for (Category c : financeSystem.getAllCategories()) {
                    if (c.getCategoryId() == category.getCategoryId()) {
                        cat = c;
                        break;
                    }
                }
                financeSystemHibernateControl.removeCategoryFromFinanceSystem(financeSystem, cat);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such category";
    }

}
