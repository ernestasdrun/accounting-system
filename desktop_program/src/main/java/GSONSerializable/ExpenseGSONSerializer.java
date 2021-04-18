package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Expense;

import java.lang.reflect.Type;

public class ExpenseGSONSerializer implements JsonSerializer<Expense> {
    @Override
    public JsonElement serialize(Expense expense, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject expJson = new JsonObject();
        expJson.addProperty("amount", expense.getAmount());
        expJson.addProperty("description", expense.getDescription());

        return expJson;
    }
}
