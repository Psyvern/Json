package json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Formatter;

public class JsonNumber implements JsonElement {

    private final Number number;

    public JsonNumber(Number number) {

        if (number != null) this.number = number;
        else throw new NullPointerException();
    }

    public Integer intValue() {

        return number.intValue();
    }

    public Long longValue() {

        return number.longValue();
    }

    public Float floatValue() {

        return number.floatValue();
    }

    public Double doubleValue() {

        return number.doubleValue();
    }

    public Byte byteValue() {

        return number.byteValue();
    }

    public Short shortValue() {

        return number.shortValue();
    }

    public BigDecimal bigDecimalValue() {

        return number instanceof BigDecimal ? (BigDecimal) number : new BigDecimal(number.toString());
    }

    public BigInteger bigIntegerValue() {

        return number instanceof BigInteger ? (BigInteger) number : new BigInteger(number.toString());
    }

    public Number numberValue() {

        return number;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {

        formatter.format("%s", number);
    }

    @Override
    public int hashCode() {

        return number.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        return obj instanceof JsonNumber && number.equals(((JsonNumber) obj).numberValue());
    }

    @Override
    public String toString() {

        return number.toString();
    }
}
