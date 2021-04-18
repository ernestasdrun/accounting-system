package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.FinanceSystem;

import java.lang.reflect.Type;

public class FinancingGSONSerializer implements JsonSerializer<FinanceSystem> {
    @Override
    public JsonElement serialize(FinanceSystem financeSystem, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject peJson = new JsonObject();
        peJson.addProperty("name", financeSystem.getName());
        peJson.addProperty("phoneNumber", financeSystem.getPhoneNumber());
        peJson.addProperty("emailAddress", financeSystem.getEmailAddress());

        return peJson;
    }
}
