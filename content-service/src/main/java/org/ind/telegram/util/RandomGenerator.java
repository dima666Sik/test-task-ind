package org.ind.telegram.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomGenerator {
    private final Random random = new Random();
    public int generate(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
