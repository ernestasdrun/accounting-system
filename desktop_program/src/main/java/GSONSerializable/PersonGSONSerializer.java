package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Person;

import java.lang.reflect.Type;

public class PersonGSONSerializer implements JsonSerializer<Person> {
    @Override
    public JsonElement serialize(Person person, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject perJson = new JsonObject();
        perJson.addProperty("id", person.getPersonId());
        perJson.addProperty("name", person.getName());
        perJson.addProperty("surname", person.getSurname());
        perJson.addProperty("phone", person.getPhoneNum());
        perJson.addProperty("email", person.getEmail());

        return perJson;
    }
}
