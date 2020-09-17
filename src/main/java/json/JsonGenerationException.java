package json;

public class JsonGenerationException extends JsonException {

    public JsonGenerationException(String message) {

        super(message);
    }

    public JsonGenerationException(String message, Throwable cause) {

        super(message, cause);
    }
}
