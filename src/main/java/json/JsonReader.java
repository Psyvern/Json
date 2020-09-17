package json;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    private final List<String> lines = new ArrayList<>();

    private int line = -1, column = -1;
    private char lastChar;
    private String currentLine = "";

    public JsonReader(String string) {

        this(new StringReader(string));
    }

    public JsonReader(Reader reader) {

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {

            while (true) {

                String line = bufferedReader.readLine();
                if (line == null) break;
                lines.add(line);
            }

        } catch (IOException e) {

            throw new JsonException("Error while reading the json", e);
        }
    }

    public JsonReader(InputStream in, Charset encoding) {

        this(new InputStreamReader(in, encoding));
    }

    public JsonReader(InputStream input) {

        this(new InputStreamReader(input));
    }

    public JsonElement read() {

        readChar();
        readWhitespaces();
        if (lastChar == '\uFFFF') return JsonElement.NULL;
        JsonElement value = readElement();
        readWhitespaces();
        if (lastChar == '\uFFFF') return value;
        throw exception("Value not allowed");
    }

    private void readWhitespaces() {

        while (Character.isWhitespace(lastChar)) readChar();
    }

    private JsonElement readElement() {

        switch (lastChar) {

            case '"': return readString();
            case '-': case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': return readNumber();
            case 'f': return readFalse();
            case 'n': return readNull();
            case 't': return readTrue();
            case '[': return readArray();
            case '{': return readObject();
            default: throw exception("Value '" + lastChar + "' not allowed");
        }
    }

    private JsonArray readArray() {

        JsonArray json = new JsonArray();

        readChar();
        readWhitespaces();
        if (lastChar == ']') {

            readChar();
            return json;
        }

        while (true) {

            json.add(readElement());

            readWhitespaces();
            if (lastChar == ',') {

                readChar();
                readWhitespaces();
                continue;
            }
            if (lastChar == ']') break;
            throw exception("There should be a ',' or a ']'");
        }

        readChar();
        return json;
    }

    private JsonElement readFalse() {

        if (readChar() == 'a' && readChar() == 'l' && readChar() == 's' && readChar() == 'e') {

            readChar();
            return JsonElement.FALSE;
        }
        throw exception("Value should be 'false'");
    }

    private JsonNumber readNumber() {

        StringBuilder output = new StringBuilder();
        output.append(lastChar);

        while (true) {

            readChar();
            if (".0123456789Ee+-".indexOf(lastChar) == -1) break;
            if (column == 0) throw exception("There shouldn't be a new line");

            output.append(lastChar);
        }

        try {

            return new JsonNumber(new BigDecimal(output.toString()));

        } catch (NumberFormatException e) {

            throw exception(e.getMessage());
        }
    }

    private JsonElement readNull() {

        if (readChar() == 'u' && readChar() == 'l' && readChar() == 'l') {

            readChar();
            return JsonElement.NULL;
        }
        throw exception("Value should be 'null'");
    }

    private JsonObject readObject() {

        JsonObject json = new JsonObject();

        while (true) {

            readChar();
            readWhitespaces();
            if (lastChar == '}') break;
            if (lastChar != '"') throw exception("There should be a '\"'");
            String name = readElementName();

            readChar();
            readWhitespaces();
            if (lastChar != ':') throw exception("There should be a ':'");

            readChar();
            readWhitespaces();
            json.put(name, readElement());

            readWhitespaces();
            if (lastChar == ',') continue;
            if (lastChar == '}') break;
            throw exception("There should be either a ',' or a '}'");
        }

        readChar();
        return json;
    }

    private JsonString readString() {

        String string = readElementName();
        readChar();
        return new JsonString(string);
    }

    private JsonElement readTrue() {

        if (readChar() == 'r' && readChar() == 'u' && readChar() == 'e') {

            readChar();
            return JsonElement.TRUE;
        }
        throw exception("Value should be 'true'");
    }

    private String readElementName() {

        StringBuilder output = new StringBuilder();

        loop: while (true) {

            readChar();
            if (column == 0) throw exception("There shouldn't be a new line");

            switch (lastChar) {
                case '\\':

                    readChar();

                    switch (lastChar) {
                        case '\"':
                            output.append('\"');
                            break;
                        case '\\':
                            output.append('\\');
                            break;
                        case '/':
                            output.append('/');
                            break;
                        case 'b':
                            output.append('\b');
                            break;
                        case 'f':
                            output.append('\f');
                            break;
                        case 'n':
                            output.append('\n');
                            break;
                        case 'r':
                            output.append('\r');
                            break;
                        case 't':
                            output.append('\t');
                            break;
                        case 'u':
                            output.append((char) Integer.parseInt(new String(new char[] {readChar(), readChar(), readChar(), readChar()}), 16));
                            break;
                        default:
                            throw exception("Illegal character: '" + lastChar + "'");
                    }
                    break;

                case '"': break loop;
                default:
                    output.append(lastChar);
                    break;
            }
        }

        return output.toString();
    }

    private char readChar() {

        column++;

        while (column >= currentLine.length()) {

            if (++line >= lines.size()) return lastChar = '\uFFFF';
            currentLine = lines.get(line);
            column = 0;
        }

        return lastChar = currentLine.charAt(column);
    }

    private JsonParsingException exception(String message) {

        return new JsonParsingException(message + " at line: " + line + ", column: " + column);
    }
}
