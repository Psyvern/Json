package json;

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

        StringBuilder sb = new StringBuilder();
        sb.append('"');

        for (char c : value.toCharArray()) {

            if (c >= 0x20 && c != 0x22 && c != 0x5c) sb.append(c);
            else switch (c) {

                case '"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append('\\');
                    sb.append('b');
                    break;
                case '\f':
                    sb.append('\\');
                    sb.append('f');
                    break;
                case '\n':
                    sb.append('\\');
                    sb.append('n');
                    break;
                case '\r':
                    sb.append('\\');
                    sb.append('r');
                    break;
                case '\t':
                    sb.append('\\');
                    sb.append('t');
                    break;
                default:
                    String hex = "000" + Integer.toHexString(c);
                    sb.append("\\u").append(hex.substring(hex.length() - 4));
            }
        }

        sb.append('"');
        return sb.toString();
    }
}
