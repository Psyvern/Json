package json;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

public class JsonWriter {

    protected int indent = 0;

    public void write(Writer writer, JsonElement json) {

        try {

            writer.append(write(json));
            writer.close();

        } catch (IOException e) {

            throw new JsonGenerationException("I/O error while generating JSON");
        }
    }

    public String write(JsonElement json) {

        if (json instanceof JsonArray) return writeArray((JsonArray) json);
        if (json instanceof JsonObject) return writeObject((JsonObject) json);
        if (json instanceof JsonNumber || json instanceof JsonString || json == JsonElement.NULL || json == JsonElement.TRUE || json == JsonElement.FALSE) return json.toString();

        throw new JsonGenerationException("Json value wrong");
    }

    String writeArray(JsonArray json) {

        StringBuilder builder = new StringBuilder("[");

        if (json.size() == 0) return builder.append("]").toString();

        indent++;
        json.forEach(element -> builder.append(writeLine()).append(write(element)).append(","));
        indent--;
        return builder.deleteCharAt(builder.length() - 1).append(writeLine()).append("]").toString();
    }

    String writeObject(JsonObject json) {

        StringBuilder builder = new StringBuilder("{");

        if (json.size() == 0) return builder.append("}").toString();

        indent++;
        json.forEach((key, value) -> builder.append(writeObjectElement(key, value)).append(","));
        indent--;
        return builder.deleteCharAt(builder.length() - 1).append(writeLine()).append("}").toString();
    }

    protected String writeObjectElement(String name, JsonElement value) {

        return writeLine() + writeElementName(name) + ":" + write(value);
    }

    protected String writeElementName(String name) {

        return '"' + name + '"';
    }

    protected String writeLine() {

        return "";
    }

    public static class Pretty extends JsonWriter {

        protected String writeObjectElement(String name, JsonElement value) {

            return writeLine() + writeElementName(name) + ": " + write(value);
        }

        @Override
        protected String writeLine() {

            return System.lineSeparator() + writeIndent();
        }

        protected String writeIndent() {

            return String.join("", Collections.nCopies(indent, "    "));
        }
    }
}
