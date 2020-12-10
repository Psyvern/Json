package json;

import java.util.FormattableFlags;
import java.util.Formatter;

public class JsonString implements JsonElement {

    private final String value;

    public JsonString(String value) {

        this.value = value;
    }

    public String getString() {

        return value;
    }

    public CharSequence getChars() {

        return value;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {

        String lj = ((flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY) ? "-" : "";
        String w = width == -1 ? "" : "." + width;
        String p = precision == -1 ? "" : String.valueOf(precision);
        String uc = ((flags & FormattableFlags.UPPERCASE) == FormattableFlags.UPPERCASE) ? "S" : "s";

        formatter.format("%" + lj + p + w + uc, value);
    }

    @Override
    public int hashCode() {

        return getString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof JsonString)) return false;
        return getString().equals(((JsonString) obj).getString());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("\"");

        for (char c : value.toCharArray()) {

            if (c >= 0x20 && c != 0x22 && c != 0x5c) sb.append(c);
            else switch (c) {

                case '\"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    String hex = "000" + Integer.toHexString(c);
                    sb.append("\\u").append(hex.substring(hex.length() - 4));
            }
        }

        sb.append("\"");
        return sb.toString();
    }
}
