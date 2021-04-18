package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Category;

import java.lang.reflect.Type;

public class CategoryGSONSerializer implements JsonSerializer<Category> {
    @Override
    public JsonElement serialize(Category category, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject catJson = new JsonObject();
        catJson.addProperty("id", category.getCategoryId());
        catJson.addProperty("name", category.getName());
        catJson.addProperty("owner", category.getOwnerName());
        //catJson.addProperty("parent", category.getParentCat());
        //catJson.addProperty("system", category.getFinanceSystem());

        return catJson;
    }
}
