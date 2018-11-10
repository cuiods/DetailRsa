package com.cuiods.cryptology.rsa.util;

import com.cuiods.cryptology.rsa.integer.MyInteger;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Util to generate random {@link MyInteger}
 * @author cuiods
 */
public class RandomUtil {

    /**
     * Random odd number of {@code bit} digits
     * @param bit bit digits
     * @return odd {@link MyInteger}
     */
    public static MyInteger randomOdd(int bit) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < bit; i++) {
            resultBuilder.append((int) (Math.random()+0.5));
        }
        String result = "1" + resultBuilder.toString().substring(1,resultBuilder.length()-1) + "1";
        return new MyInteger(result, 2);
    }

    /**
     * Random Integer with Bound
     * @param lowerBound lowerBound
     * @param upperBound upperBound
     * @return {@link MyInteger}
     */
    public static MyInteger randomInteger(MyInteger lowerBound, MyInteger upperBound) {
        Random random = new SecureRandom();
        MyInteger result = new MyInteger(upperBound.length(), random).mod(upperBound);
        if (result.compareAbs(lowerBound) < 0)
            result = lowerBound;
        return result;
    }
}
