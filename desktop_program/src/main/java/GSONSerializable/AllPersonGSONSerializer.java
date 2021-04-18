package GSONSerializable;

import com.google.gson.*;
import model.Person;

import java.lang.reflect.Type;
import java.util.List;

public class AllPersonGSONSerializer implements JsonSerializer<List<Person>> {
    @Override
    public JsonElement serialize(List<Person> people, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Person.class, new PersonGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Person p : people) {
            jsonArray.add(parser.toJson(p));
        }
        return jsonArray;
    }
}
