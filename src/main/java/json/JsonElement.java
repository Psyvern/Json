package json;

import java.util.Formattable;
import java.util.Formatter;

public interface JsonElement extends Formattable {

    JsonObject EMPTY_OBJECT = new JsonObject();
    JsonArray EMPTY_ARRAY = new JsonArray();
    JsonElement NULL = new JsonElement() {

        @Override
        public void formatTo(Formatter formatter, int flags, int width, int precision) {

            formatter.format(toString());
        }

        @Override
        public String toString() {

            return "null";
        }
    };
    JsonElement TRUE = new JsonElement() {

        @Override
        public void formatTo(Formatter formatter, int flags, int width, int precision) {

            formatter.format(toString());
        }

        @Override
        public String toString() {

            return "true";
        }
    };
    JsonElement FALSE = new JsonElement() {

        @Override
        public void formatTo(Formatter formatter, int flags, int width, int precision) {

            formatter.format(toString());
        }

        @Override
        public String toString() {

            return "false";
        }
    };
}
