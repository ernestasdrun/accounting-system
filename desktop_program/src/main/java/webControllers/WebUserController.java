package webControllers;

import GSONSerializable.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hibernate.CompanyHibernateControl;
import hibernate.FinanceSystemHibernateControl;
import hibernate.PersonHibernateControl;
import model.Company;
import model.FinanceSystem;
import model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
public class WebUserController {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);
    CompanyHibernateControl companyHibernateControl = new CompanyHibernateControl(entityManagerFactory);
    PersonHibernateControl personHibernateControl = new PersonHibernateControl(entityManagerFactory);

    @RequestMapping(value = "user/personLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginPerson(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        Person person = personHibernateControl.findPerson(loginName, password);

        if (person == null) {
            return "Wrong credentials";
        }
        return Integer.toString(person.getPersonId());
    }


    @RequestMapping(value = "user/companyLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginCompany(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        Company company = companyHibernateControl.findCompany(loginName, password);

        if (company == null) {
            return "Wrong credentials";
        }
        return Integer.toString(company.getCompanyId());
    }


    @RequestMapping(value = "user/personList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String personList() {

        List<Person> pList = personHibernateControl.findPersonEntities();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Person.class, new PersonGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(pList.get(0));

        Type personList = new TypeToken<List<Person>>() {
        }.getType();
        gson.registerTypeAdapter(personList, new AllPersonGSONSerializer());
        parser = gson.create();

        return parser.toJson(pList);
    }


    @RequestMapping(value = "user/companyList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String companyList() {

        List<Company> cList = companyHibernateControl.findCompanyEntities();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Company.class, new CompanyGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(cList.get(0));

        Type companyList = new TypeToken<List<Company>>() {
        }.getType();
        gson.registerTypeAdapter(companyList, new AllCompanyGSONSerializer());
        parser = gson.create();

        return parser.toJson(cList);
    }


    @RequestMapping(value = "user/personInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getPersonInfoByLogin(@RequestParam("login") String login) {

        if (login.equals("")) return "No person login provided";

        Person person = personHibernateControl.findPerson(login);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Person.class, new PersonGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(person);
    }


    @RequestMapping(value = "user/companyInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCompanyInfoByLogin(@RequestParam("login") String login) {

        if (login.equals("")) return "No company login provided";

        Company company = companyHibernateControl.findCompany(login);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Company.class, new CompanyGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(company);
    }


    @RequestMapping(value = "user/updateInfoPerson", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updatePerson(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String surname = data.getProperty("surname");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String userId = data.getProperty("id");

        Person person = personHibernateControl.findPerson(Integer.parseInt(userId));

        if (person != null) {
            person.setName(name);
            person.setSurname(surname);
            person.setEmail(email);
            person.setPhoneNum(phone);
            try {
                personHibernateControl.edit(person);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such person";
    }


    @RequestMapping(value = "user/updateInfoCompany", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCompany(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String address = data.getProperty("address");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String userId = data.getProperty("id");

        Company company = companyHibernateControl.findCompany(Integer.parseInt(userId));

        if (company != null) {
            company.setName(name);
            company.setAddress(address);
            company.setEmail(email);
            company.setPhoneNum(phone);
            try {
                companyHibernateControl.edit(company);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such company";
    }


    @RequestMapping(value = "user/createCompany", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCompany(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String psw = data.getProperty("psw");
        String loginName = data.getProperty("login");
        String phone = data.getProperty("phone");
        String email = data.getProperty("email");
        String address = data.getProperty("address");
        String system = data.getProperty("system");

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(system);

        if (financeSystem != null) {
            try {
                companyHibernateControl.create(new Company(name, psw, loginName, phone, email, address, true, null, financeSystem));
                financeSystemHibernateControl.AddCompanyToFinanceSystem(financeSystem, companyHibernateControl.findCompany(loginName));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "No such system";
    }


    @RequestMapping(value = "user/createPerson", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createPerson(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String psw = data.getProperty("psw");
        String loginName = data.getProperty("login");
        String surname = data.getProperty("surname");
        String phone = data.getProperty("phone");
        String email = data.getProperty("email");
        String system = data.getProperty("system");

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(system);

        if (financeSystem != null) {
            try {
                personHibernateControl.create(new Person(name, psw, loginName, surname, phone, email, true, null, financeSystem));
                financeSystemHibernateControl.AddPersonToFinanceSystem(financeSystem, personHibernateControl.findPerson(loginName));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "No such system";
    }


    @RequestMapping(value = "user/deletePerson", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deletePerson(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userId = data.getProperty("id");

        Person person = personHibernateControl.findPerson(Integer.parseInt(userId));

        if (person != null) {
            try {
                personHibernateControl.destroy(Integer.parseInt(userId));
                FinanceSystem financeSystem = person.getFinanceSystem();
                financeSystemHibernateControl.removePersonFromFinanceSystem(financeSystem, person);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such person";
    }


    @RequestMapping(value = "user/deleteCompany", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCompany(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userId = data.getProperty("id");

        Company company = companyHibernateControl.findCompany(Integer.parseInt(userId));

        if (company != null) {
            try {
                companyHibernateControl.destroy(Integer.parseInt(userId));
                FinanceSystem financeSystem = company.getFinanceSystem();
                financeSystemHibernateControl.removeCompanyFromFinanceSystem(financeSystem, company);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such company";
    }

}
