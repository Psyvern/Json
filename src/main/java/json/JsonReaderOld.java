package json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;

public class JsonReaderOld {

    private int line = 1, column = 1;
    private int currentChar = 0, lastChar = ' ';
    private final String string;

    public JsonReaderOld(String string) {

        this.string = string + (char) -1;
    }

    public JsonReaderOld(Reader reader) {

        StringBuilder builder = new StringBuilder();

        try {

            while (true) {

                int character = reader.read();
                builder.append((char) character);
                if (character == -1) break;
            }

            reader.close();

        } catch (IOException e) {

            throw new JsonException("eRrOrE", e);
        }

        string = builder.toString();
    }

    public JsonReaderOld(InputStream in, Charset encoding) {

        this(new InputStreamReader(in, encoding));
    }

    public JsonReaderOld(InputStream input) {

        this(new InputStreamReader(input));
    }

    public JsonElement read() {

        readWhitespaces();
        JsonElement value = readElement();
        readChar();
        readWhitespaces();
        if (lastChar == 65535) return value;
        throw exception("Value not allowed");
    }

    public void readWhitespaces() {

        while (Character.isWhitespace(lastChar)) readChar();
    }

    public JsonElement readElement() {

        switch (lastChar) {

            case '"': return readString();
            case '-': case '.': case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': return readNumber();
            case 'f': return readFalse();
            case 'n': return readNull();
            case 't': return readTrue();
            case '[': return readArray();
            case '{': return readObject();
        }

        throw exception("Value '" + (char) lastChar + "' not allowed");
    }

    public JsonArray readArray() {

        JsonArray json = new JsonArray();

        while (true) {

            readChar();
            readWhitespaces();
            if (lastChar == ']') break;
            json.add(readElement());

            readChar();
            readWhitespaces();
            if (lastChar == ',') continue;
            if (lastChar == ']') break;
            throw exception("There should be a ',' or a ']'");
        }

        return json;
    }

    public JsonElement readFalse() {

        if (readChar() == 'a' && readChar() == 'l' && readChar() == 's' && readChar() == 'e') return JsonElement.FALSE;
        throw exception("Value should be 'false'");
    }

    public JsonNumber readNumber() {

        StringBuilder output = new StringBuilder();
        output.append((char) lastChar);

        while (true) {

            readChar();
            //if (lastChar == '\n') throw exception("There shouldn't be a new line");
            if ("-.0123456789".indexOf(lastChar) == -1) {

                currentChar--;
                break;
            }

            output.append((char) lastChar);
        }

        return new JsonNumber(new BigDecimal(output.toString()));
    }

    public JsonElement readNull() {

        if (readChar() == 'u' && readChar() == 'l' && readChar() == 'l') return JsonElement.NULL;
        throw exception("Value should be 'null'");
    }

    public JsonObject readObject() {

        JsonObject json = new JsonObject();

        while (true) {

            readChar();
            readWhitespaces();
            if (lastChar == '}') break;
            String name = readElementName();

            readChar();
            readWhitespaces();
            if (lastChar != ':') throw exception("There should be a ':'");

            readChar();
            readWhitespaces();
            json.put(name, readElement());

            readChar();
            readWhitespaces();
            if (lastChar == ',') continue;
            if (lastChar == '}') break;
            throw exception("There should be either a ',' or a '}'");
        }

        return json;
    }

    public JsonString readString() {

        StringBuilder output = new StringBuilder();

        while (true) {

            readChar();
            if (lastChar == '\n') throw exception("There shouldn't be a new line");
            if (lastChar == '"') break;
            output.append((char) lastChar);
        }

        return new JsonString(output.toString());
    }

    public JsonElement readTrue() {

        if (readChar() == 'r' && readChar() == 'u' && readChar() == 'e') return JsonElement.TRUE;
        throw exception("Value should be 'true'");
    }

    public String readElementName() {

        StringBuilder output = new StringBuilder();

        if (lastChar != '"') throw exception("There should be a '" + '"' + "'");

        while (true) {

            readChar();
            if (lastChar == '\n') throw exception("There shouldn't be a new line");
            if (lastChar == '"') break;
            output.append((char) lastChar);
        }

        return output.toString();
    }

    private int readChar() {

        lastChar = string.codePointAt(currentChar);
        currentChar++;

        if (lastChar == '\n') {

            line++;
            column = 1;

        } else column++;

        return lastChar;
    }

    private JsonParsingException exception(String message) {

        return new JsonParsingException(message + " at line: " + line + ", column: " + (column - 1));
    }
}
