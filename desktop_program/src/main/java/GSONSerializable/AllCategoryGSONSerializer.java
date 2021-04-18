package GSONSerializable;

import com.google.gson.*;
import model.Category;
import model.Company;

import java.lang.reflect.Type;
import java.util.List;

public class AllCategoryGSONSerializer implements JsonSerializer<List<Category>> {
    @Override
    public JsonElement serialize(List<Category> categories, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Category.class, new CategoryGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Category c : categories) {
            jsonArray.add(parser.toJson(c));
        }
        return jsonArray;
    }
}
