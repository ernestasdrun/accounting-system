package GSONSerializable;

import com.google.gson.*;
import model.Expense;

import java.lang.reflect.Type;
import java.util.List;

public class AllExpenseGSONSerializer implements JsonSerializer<List<Expense>> {
    @Override
    public JsonElement serialize(List<Expense> expenses, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Expense.class, new ExpenseGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Expense e : expenses) {
            jsonArray.add(parser.toJson(e));
        }
        return jsonArray;
    }
}
