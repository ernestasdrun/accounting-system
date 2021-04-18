package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Company;

import java.lang.reflect.Type;

public class CompanyGSONSerializer implements JsonSerializer<Company> {
    @Override
    public JsonElement serialize(Company company, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject comJson = new JsonObject();
        comJson.addProperty("id", company.getCompanyId());
        comJson.addProperty("name", company.getName());
        comJson.addProperty("phone", company.getPhoneNum());
        comJson.addProperty("email", company.getEmail());
        comJson.addProperty("address", company.getAddress());

        return comJson;
    }
}
