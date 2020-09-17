package json;

import java.util.function.Function;

class JsonDeserializer<T> {

    public final Class<T> clazz;
    public final Function<JsonElement, ? extends T> function;

    public JsonDeserializer(Class<T> clazz, Function<JsonElement, ? extends T> function) {

        this.clazz = clazz;
        this.function = function;
    }
}
