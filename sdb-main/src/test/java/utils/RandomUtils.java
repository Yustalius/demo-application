package utils;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static long randomNumber(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1");
        }
        long min = (long) Math.pow(10, length - 1);
        long max = (long) Math.pow(10, length) - 1;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static int randomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(collection.size());
        return collection.stream()
            .skip(index)
            .findFirst()
            .orElse(null);
    }
}