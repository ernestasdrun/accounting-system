package GSONSerializable;

import com.google.gson.*;
import model.FinanceSystem;

import java.lang.reflect.Type;
import java.util.List;

public class AllFinancingGSONSerializer implements JsonSerializer<List<FinanceSystem>> {
    @Override
    public JsonElement serialize(List<FinanceSystem> financeSystems, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FinanceSystem.class, new FinancingGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (FinanceSystem f : financeSystems) {
            jsonArray.add(parser.toJson(f));
        }
        return jsonArray;
    }
}
