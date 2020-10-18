package json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class JsonArray extends ArrayList<JsonElement> implements JsonElement, Formattable {

    public JsonArray(Collection<?> collection) {

        for (Object value : collection) {

            if (value instanceof Optional) ((Optional<?>) value).ifPresent(v -> add(Json.serialize(v)));
            else add(Json.serialize(value));
        }
    }

    public JsonArray() {

    }

    @Nonnull
    public JsonElement get(int index) {

        return Objects.requireNonNull(super.get(index));
    }

    @Nonnull
    public JsonArray getJsonArray(int index) {

        return (JsonArray) get(index);
    }

    @Nonnull
    public JsonArray getJsonArray(int index, @Nonnull JsonArray defaultValue) {

        try {

            return getJsonArray(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    @Nonnull
    public JsonArray getJsonArraySafe(int index) {

        return getJsonArray(index, EMPTY_ARRAY);
    }

    @Nonnull
    public JsonObject getJsonObject(int index) {

        return (JsonObject) get(index);
    }

    @Nonnull
    public JsonObject getJsonObject(int index, @Nonnull JsonObject defaultValue) {

        try {

            return getJsonObject(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    @Nonnull
    public JsonObject getJsonObjectSafe(int index) {

        return getJsonObject(index, EMPTY_OBJECT);
    }

    @Nonnull
    public JsonNumber getJsonNumber(int index) {

        return (JsonNumber) get(index);
    }

    @Nonnull
    public JsonString getJsonString(int index) {

        return (JsonString) get(index);
    }

    public String getString(int index) {

        return getJsonString(index).getString();
    }

    public String getString(int index, @Nonnull String defaultValue) {

        try {

            return getString(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public String getStringSafe(int index) {

        return getString(index, "");
    }

    public int getInt(int index) {

        return getJsonNumber(index).intValue();
    }

    public int getInt(int index, int defaultValue) {

        try {

            return getInt(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public int getIntSafe(int index) {

        return getInt(index, 0);
    }

    public long getLong(int index) {

        return getJsonNumber(index).longValue();
    }

    public long getLong(int index, long defaultValue) {

        try {

            return getLong(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public long getLongSafe(int index) {

        return getLong(index, 0);
    }

    public float getFloat(int index) {

        return getJsonNumber(index).floatValue();
    }

    public float getFloat(int index, float defaultValue) {

        try {

            return getFloat(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public float getFloatSafe(int index) {

        return getFloat(index, 0);
    }

    public double getDouble(int index) {

        return getJsonNumber(index).doubleValue();
    }

    public double getDouble(int index, double defaultValue) {

        try {

            return getDouble(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public double getDoubleSafe(int index) {

        return getDouble(index, 0);
    }

    public byte getByte(int index) {

        return getJsonNumber(index).byteValue();
    }

    public byte getByte(int index, byte defaultValue) {

        try {

            return getByte(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public byte getByteSafe(int index) {

        return getByte(index, (byte) 0);
    }

    public short getShort(int index) {

        return getJsonNumber(index).shortValue();
    }

    public short getShort(int index, short defaultValue) {

        try {

            return getShort(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public short getShortSafe(int index) {

        return getShort(index, (short) 0);
    }

    public boolean getBoolean(int index) {

        JsonElement value = get(index);
        if (value == TRUE) return true;
        if (value == FALSE) return false;
        throw new ClassCastException();
    }

    public boolean getBoolean(int index, boolean defaultValue) {

        try {

            return getBoolean(index);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public boolean getBooleanSafe(int index) {

        return getBoolean(index, false);
    }

    @Nullable
    public <T> T getAny(int index, Class<T> clazz) {

        return Json.deserialize(get(index), clazz);
    }

    public boolean isNull(int index) {

        return get(index) == NULL;
    }

    @Override
    public boolean add(JsonElement value) {

        return super.add(value == null ? JsonElement.NULL : value);
    }

    @Nonnull
    public JsonArray addAny(Object value) {

        super.add(Json.serialize(value));
        return this;
    }

    @Override
    public void add(int index, JsonElement value) {

        super.add(index, value == null ? JsonElement.NULL : value);
    }

    @Nonnull
    public JsonArray addAny(int index, @Nullable Object value) {

        super.add(index, Json.serialize(value));
        return this;
    }

    @Override
    public JsonElement set(int index, JsonElement value) {

        return super.set(index, value == null ? JsonElement.NULL : value);
    }

    @Nonnull
    public JsonArray setAny(int index, @Nullable Object value) {

        super.set(index, Json.serialize(value));
        return this;
    }

    @Nonnull
    public JsonArray removeAny(int index) {

        super.remove(index);
        return this;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {

        StringBuilder builder = new StringBuilder();
        String out = ((flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE ? new JsonWriter() : new JsonWriter.Pretty()).writeArray(this);

        if (precision == -1 || out.length() < precision) builder.append(out);
        else builder.append(out, 0, precision - 1).append('*');

        int length = builder.length();
        if (length < width) for (int i = 0; i < width - length; i++) {

            if ((flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY) builder.append(' ');
            else builder.insert(0, ' ');
        }

        formatter.format(builder.toString());
    }

    @Override
    public String toString() {

        return new JsonWriter().writeArray(this);
    }
}
