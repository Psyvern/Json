package json;

import java.util.function.Function;

class JsonSerializer<T> {

    public final Class<T> clazz;
    public final Function<? extends T, JsonElement> function;

    public JsonSerializer(Class<T> clazz, Function<? extends T, JsonElement> function) {

        this.clazz = clazz;
        this.function = function;
    }
}
