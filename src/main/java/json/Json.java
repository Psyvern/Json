package json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

//@SuppressWarnings("unchecked")
public class Json {

    private static final List<JsonSerializer<?>> serializers = new ArrayList<>();

    static {

        addSerializer(JsonElement.class, value -> value);
        addSerializer(Number.class, JsonNumber::new);
        addSerializer(Boolean.class, value -> value ? JsonElement.TRUE : JsonElement.FALSE);
        addSerializer(String.class, JsonString::new);
        addSerializer(Collection.class, JsonArray::new);
        addSerializer(Map.class, JsonObject::new);
    }

    public static <T> void addSerializer(Class<T> clazz, Function<? extends T, JsonElement> function) {

        serializers.add(new JsonSerializer<>(clazz, function));
    }

    @Nonnull
    public static JsonElement serialize(@Nullable Object value) {

        if (value == null) return JsonElement.NULL;
        for (JsonSerializer serializer : serializers) if (serializer.clazz.isInstance(value)) return (JsonElement) serializer.function.apply(value);
        if (value instanceof Enum) return new JsonString(((Enum<?>) value).name());
        throw new IllegalArgumentException("Type " + value.getClass() + " is not supported.");
    }

    public static <T> T deserialize(@Nonnull JsonElement json, @Nonnull Class<T> clazz) {

        if (json instanceof JsonNumber) return deserialize((JsonNumber) json, clazz);
        if (json instanceof JsonString) return deserialize((JsonString) json, clazz);
        if (json instanceof JsonObject) return deserialize((JsonObject) json, clazz);
        if (json instanceof JsonArray) return deserialize((JsonArray) json, clazz);
        if (json == JsonElement.TRUE) return (T) Boolean.TRUE;
        if (json == JsonElement.FALSE) return (T) Boolean.FALSE;
        return null;
    }

    private static <T> T deserialize(@Nonnull JsonNumber json, @Nonnull Class<T> clazz) {

        if (clazz == Integer.class) return (T) json.intValue();
        if (clazz == Long.class) return (T) json.longValue();
        if (clazz == Float.class) return (T) json.floatValue();
        if (clazz == Double.class) return (T) json.doubleValue();
        if (clazz == Byte.class) return (T) json.byteValue();
        if (clazz == Short.class) return (T) json.shortValue();
        if (clazz == BigDecimal.class) return (T) json.bigDecimalValue();
        if (clazz == BigInteger.class) return (T) json.bigIntegerValue();
        if (clazz == Number.class) return (T) json.numberValue();

        if (clazz == Instant.class) return (T) Instant.ofEpochMilli(json.longValue());
        if (clazz.isEnum()) return clazz.getEnumConstants()[json.intValue()];
        return null;
    }

    private static <T> T deserialize(@Nonnull JsonString json, @Nonnull Class<T> clazz) {

        if (clazz == String.class) return (T) json.getString();
        if (clazz == CharSequence.class) return (T) json.getString();
        if (clazz.isEnum()) for (T constant : clazz.getEnumConstants()) if (constant.toString().equals(json.getString())) return constant;
        return null;
    }

    private static <T> T deserialize(@Nonnull JsonObject json, @Nonnull Class<T> clazz) {

        try {

            T object = clazz.getConstructor().newInstance();

            for (Field field : clazz.getFields()) {

                if (json.contains(field.getName())) {

                    field.set(object, deserialize(json.get(field.getName()), field.getType()));
                }
            }

            return object;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private static <T> T deserialize(@Nonnull JsonArray json, @Nonnull Class<T> clazz) {

        if (clazz == List.class && clazz.getGenericSuperclass() instanceof ParameterizedType) {

            Type argument = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];

            List<Object> list = new ArrayList<>();
            for (JsonElement element : json) list.add(deserialize(element, (Class<?>) argument));
            return (T) list;
        }

        return null;
    }

    public static <T> List<T> deserializeList(@Nonnull JsonArray json, @Nonnull Class<T> clazz) {

        List<T> list = new ArrayList<>();
        for (JsonElement element : json) list.add(deserialize(element, clazz));
        return list;
    }
}
