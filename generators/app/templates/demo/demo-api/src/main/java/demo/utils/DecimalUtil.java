package <%= package %>.<%= project %>.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author <%= user %>
 */
public class DecimalUtil {
    private DecimalUtil() {
    }

    private static final String DEFAULT_PARSE = "#.##";

    /**
     * 解决BigDecimal类型数据toString方法返回的stringCache会出现0.00的情况
     */
    public static String parseBigDecimal(BigDecimal value) {
        return parseBigDecimal(value, DEFAULT_PARSE);
    }

    public static String parseBigDecimal(BigDecimal value, String parse) {
        if (value == null) {
            return "";
        }
        DecimalFormat df = new DecimalFormat(parse);
        return df.format(value);
    }

}
