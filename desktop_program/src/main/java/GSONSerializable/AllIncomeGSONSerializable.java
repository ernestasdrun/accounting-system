package GSONSerializable;

import com.google.gson.*;
import model.Income;

import java.lang.reflect.Type;
import java.util.List;

public class AllIncomeGSONSerializable implements JsonSerializer<List<Income>> {
    @Override
    public JsonElement serialize(List<Income> incomes, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Income.class, new IncomeGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Income i : incomes) {
            jsonArray.add(parser.toJson(i));
        }
        return jsonArray;
    }
}
