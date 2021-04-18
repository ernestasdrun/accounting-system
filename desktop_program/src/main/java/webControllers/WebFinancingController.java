package webControllers;

import GSONSerializable.AllFinancingGSONSerializer;
import GSONSerializable.FinancingGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hibernate.FinanceSystemHibernateControl;
import model.FinanceSystem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
public class WebFinancingController {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FinancingSystemHib");
    FinanceSystemHibernateControl financeSystemHibernateControl = new FinanceSystemHibernateControl(entityManagerFactory);

    @RequestMapping(value = "financingSystem/sysList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllSystems() {

        List<FinanceSystem> allSyst = financeSystemHibernateControl.findFinanceSystemEntities();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(FinanceSystem.class, new FinancingGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allSyst.get(0));

        Type systemList = new TypeToken<List<FinanceSystem>>() {
        }.getType();
        gson.registerTypeAdapter(systemList, new AllFinancingGSONSerializer());
        parser = gson.create();

        return parser.toJson(allSyst);
    }


    @RequestMapping(value = "financingSystem/sysInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getSystemInfoByName(@RequestParam("name") String name) {

        if (name.equals("")) return "No system name provided";

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(name);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(FinanceSystem.class, new FinancingGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(financeSystem);
    }


    @RequestMapping(value = "financingSystem/updateSystem", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateSystem(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String phone = data.getProperty("phone");
        String email = data.getProperty("email");

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(name);

        if (financeSystem != null) {
            financeSystem.setName(name);
            financeSystem.setPhoneNumber(phone);
            financeSystem.setEmailAddress(email);
            try {
                financeSystemHibernateControl.edit(financeSystem);
            } catch (Exception e) {
                return "Error while updating";
            }
            return "Update successful";
        } else return "No such system";
    }


    @RequestMapping(value = "financingSystem/createSystem", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createSystem(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String phone = data.getProperty("phone");
        String email = data.getProperty("email");

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(name);

        if (financeSystem == null) {
            try {
                financeSystemHibernateControl.create(new FinanceSystem(name, phone, email, null, null, null));
            } catch (Exception e) {
                return "Error while creating";
            }
            return "Created successfully";
        } else return "System with this name already exists";
    }


    @RequestMapping(value = "financingSystem/deleteSystem", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteSystem(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");

        FinanceSystem financeSystem = financeSystemHibernateControl.findFinanceSystem(name);

        if (financeSystem != null) {
            try {
                financeSystemHibernateControl.destroy(name);
            } catch (Exception e) {
                return "Error while deleting";
            }
            return "Deleted successfully";
        }
        return "No such system";
    }

}
