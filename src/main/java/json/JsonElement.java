package json;

public interface JsonElement {

    JsonObject EMPTY_OBJECT = new JsonObject();
    JsonArray EMPTY_ARRAY = new JsonArray();
    JsonElement NULL = new JsonElement() {

        @Override
        public String toString() {

            return "null";
        }
    };
    JsonElement TRUE = new JsonElement() {

        @Override
        public String toString() {

            return "true";
        }
    };
    JsonElement FALSE = new JsonElement() {

        @Override
        public String toString() {

            return "false";
        }
    };
}
