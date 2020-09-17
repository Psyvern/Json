package json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class Json {

    private static final List<JsonSerializer<?>> serializers = new ArrayList<>();
    private static final List<JsonDeserializer<?>> deserializers = new ArrayList<>();

    static {

        addSerializer(JsonElement.class, value -> value);
        addSerializer(Number.class, JsonNumber::new);
        addSerializer(Boolean.class, value -> value ? JsonElement.TRUE : JsonElement.FALSE);
        addSerializer(String.class, JsonString::new);
        addSerializer(Collection.class, JsonArray::new);
        addSerializer(Map.class, JsonObject::new);

        addDeserializer(JsonElement.class, element -> element);
        addDeserializer(Integer.class, element -> ((JsonNumber) element).intValue());
        addDeserializer(Long.class, element -> ((JsonNumber) element).longValue());
        addDeserializer(Float.class, element -> ((JsonNumber) element).floatValue());
        addDeserializer(Double.class, element -> ((JsonNumber) element).doubleValue());
        addDeserializer(Byte.class, element -> ((JsonNumber) element).byteValue());
        addDeserializer(Short.class, element -> ((JsonNumber) element).shortValue());
        addDeserializer(Boolean.class, element -> element == JsonElement.TRUE);
        addDeserializer(String.class, element -> ((JsonString) element).getString());
        addDeserializer(Collection.class, element -> (JsonArray) element);
        addDeserializer(Map.class, element -> (JsonObject) element);
    }

    public static <T> void addSerializer(Class<T> clazz, Function<? extends T, JsonElement> function) {

        serializers.add(new JsonSerializer<>(clazz, function));
    }

    public static <T> void addDeserializer(Class<T> clazz, Function<JsonElement, ? extends T> function) {

        deserializers.add(new JsonDeserializer<>(clazz, function));
    }

    @Nonnull
    public static JsonElement serialize(@Nullable Object value) {

        if (value == null) return JsonElement.NULL;
        for (JsonSerializer serializer : serializers) if (serializer.clazz.isInstance(value)) return (JsonElement) serializer.function.apply(value);
        if (value instanceof Enum) return new JsonString(((Enum<?>) value).name());
        throw new IllegalArgumentException("Type " + value.getClass() + " is not supported.");
    }

    @Nullable
    public static <T, S extends Enum<S>> T deserialize(@Nonnull JsonElement value, Class<T> clazz) {

        if (value == JsonElement.NULL) return null;
        for (JsonDeserializer<?> deserializer : deserializers) if (clazz.isAssignableFrom(deserializer.clazz)) return (T) deserializer.function.apply(value);
        if (clazz.isEnum()) return (T) Enum.valueOf((Class<S>) clazz, ((JsonString) value).getString());
        throw new IllegalArgumentException("Type " + value.getClass() + " is not supported.");
    }
}
