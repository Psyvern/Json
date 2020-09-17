package json;

public class JsonParsingException extends JsonException {

    public JsonParsingException(String message) {

        super(message);
    }

    public JsonParsingException(String message, Throwable cause) {

        super(message, cause);
    }
}
