package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Income;

import java.lang.reflect.Type;

public class IncomeGSONSerializer implements JsonSerializer<Income> {
    @Override
    public JsonElement serialize(Income income, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject incJson = new JsonObject();
        incJson.addProperty("amount", income.getAmount());
        incJson.addProperty("description", income.getDescription());

        return incJson;
    }
}
