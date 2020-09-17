package json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class JsonObject extends LinkedHashMap<String, JsonElement> implements JsonElement {

    public JsonObject(Map<?, ?> map) {

        super();

        map.forEach((field, value) -> {

            if (field instanceof String) {

                if (value instanceof Optional) ((Optional<?>) value).ifPresent(v -> put((String) field, Json.serialize(v)));
                else put((String) field, Json.serialize(value));
            }
        });
    }

    public JsonObject(JsonObject json) {

        super(json);
    }

    public JsonObject() {

        super();
    }

    @Nonnull
    public JsonElement get(@Nonnull String name) {

        return Objects.requireNonNull(super.get(name));
    }

    @Nonnull
    public JsonElement get(@Nonnull String name, @Nonnull JsonElement defaultValue) {

        return getOrDefault(name, defaultValue);
    }

    @Nonnull
    public JsonArray getJsonArray(@Nonnull String name) {

        return (JsonArray) get(name);
    }

    @Nonnull
    public JsonArray getJsonArray(@Nonnull String name, @Nonnull JsonArray defaultValue) {

        try {

            return getJsonArray(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    @Nonnull
    public JsonArray getJsonArraySafe(@Nonnull String name) {

        return getJsonArray(name, EMPTY_ARRAY);
    }

    @Nonnull
    public JsonObject getJsonObject(@Nonnull String name) {

        return (JsonObject) get(name);
    }

    @Nonnull
    public JsonObject getJsonObject(@Nonnull String name, @Nonnull JsonObject defaultValue) {

        try {

            return getJsonObject(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    @Nonnull
    public JsonObject getJsonObjectSafe(@Nonnull String name) {

        return getJsonObject(name, EMPTY_OBJECT);
    }

    @Nonnull
    public JsonNumber getJsonNumber(@Nonnull String name) {

        return (JsonNumber) get(name);
    }

    @Nonnull
    public JsonString getJsonString(@Nonnull String name) {

        return (JsonString) get(name);
    }

    public String getString(@Nonnull String name) {

        return getJsonString(name).getString();
    }

    public String getString(@Nonnull String name, @Nonnull String defaultValue) {

        try {

            return getString(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public String getStringSafe(@Nonnull String name) {

        return getString(name, "");
    }

    public int getInt(@Nonnull String name) {

        return getJsonNumber(name).intValue();
    }

    public int getInt(@Nonnull String name, int defaultValue) {

        try {

            return getInt(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public int getIntSafe(@Nonnull String name) {

        return getInt(name, 0);
    }

    public long getLong(@Nonnull String name) {

        return getJsonNumber(name).longValue();
    }

    public long getLong(@Nonnull String name, long defaultValue) {

        try {

            return getLong(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public long getLongSafe(@Nonnull String name) {

        return getLong(name, 0);
    }

    public float getFloat(@Nonnull String name) {

        return getJsonNumber(name).floatValue();
    }

    public float getFloat(@Nonnull String name, float defaultValue) {

        try {

            return getFloat(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public float getFloatSafe(@Nonnull String name) {

        return getFloat(name, 0);
    }

    public double getDouble(@Nonnull String name) {

        return getJsonNumber(name).doubleValue();
    }

    public double getDouble(@Nonnull String name, double defaultValue) {

        try {

            return getDouble(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public double getDoubleSafe(@Nonnull String name) {

        return getDouble(name, 0);
    }

    public byte getByte(@Nonnull String name) {

        return getJsonNumber(name).byteValue();
    }

    public byte getByte(@Nonnull String name, byte defaultValue) {

        try {

            return getByte(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public byte getByteSafe(@Nonnull String name) {

        return getByte(name, (byte) 0);
    }

    public short getShort(@Nonnull String name) {

        return getJsonNumber(name).shortValue();
    }

    public short getShort(@Nonnull String name, short defaultValue) {

        try {

            return getShort(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public short getShortSafe(@Nonnull String name) {

        return getShort(name, (short) 0);
    }

    public boolean getBoolean(@Nonnull String name) {

        JsonElement value = get(name);
        if (value == TRUE) return true;
        if (value == FALSE) return false;
        throw new ClassCastException();
    }

    public boolean getBoolean(@Nonnull String name, boolean defaultValue) {

        try {

            return getBoolean(name);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public boolean getBooleanSafe(@Nonnull String name) {

        return getBoolean(name, false);
    }

    @Nullable
    public <T> T getAny(@Nonnull String name, Class<T> clazz) {

        return Json.deserialize(get(name), clazz);
    }

    public boolean isNull(@Nonnull String name) {

        return get(name) == NULL;
    }

    @Override
    public JsonElement put(String name, JsonElement value) {

        return super.put(name, value == null ? JsonElement.NULL : value);
    }

    @Nonnull
    public JsonObject putAny(@Nonnull String name, @Nullable Object value) {

        super.put(name, Json.serialize(value));
        return this;
    }

    public boolean contains(@Nonnull String name) {

        return containsKey(name);
    }

    public boolean contains(@Nonnull JsonElement value) {

        return containsValue(value);
    }

    @Nonnull
    public JsonObject remove(@Nonnull String name) {

        super.remove(name);
        return this;
    }

    @Override
    public String toString() {

        return new JsonWriter().writeObject(this);
    }
}
