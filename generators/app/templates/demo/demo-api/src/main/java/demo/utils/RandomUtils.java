package <%= package %>.<%= project %>.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <%= user %>
 */
public class RandomUtils {

    private RandomUtils() {
    }

    /**
     * 产生2位随机数
     *
     * @param max
     * @param min
     * @return
     */
    public static int random(int max, int min) {
        return ThreadLocalRandom.current().nextInt() * (max - min + 1) + min;
    }

    public static int random() {
        int max = 99;
        int min = 10;
        return random(max, min);
    }

    public static String getNumberRandom(int len) {
        String            source            = "0123456789";
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        StringBuilder     rs                = new StringBuilder();
        for (int j = 0; j < len; j++) {
            rs.append(source.charAt(threadLocalRandom.nextInt(10)));
        }
        return rs.toString();
    }
}
