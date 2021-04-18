package GSONSerializable;

import com.google.gson.*;
import model.Company;

import java.lang.reflect.Type;
import java.util.List;

public class AllCompanyGSONSerializer implements JsonSerializer<List<Company>> {
    @Override
    public JsonElement serialize(List<Company> companies, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Company.class, new CompanyGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Company c : companies) {
            jsonArray.add(parser.toJson(c));
        }
        return jsonArray;
    }
}
