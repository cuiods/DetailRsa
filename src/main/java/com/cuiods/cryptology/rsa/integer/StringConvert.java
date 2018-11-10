package com.cuiods.cryptology.rsa.integer;

import java.math.BigInteger;

public class StringConvert {

    /**
     * Radix convert
     * @param num num string
     * @param inRadix in radix
     * @param outRadix out radix
     * @return string
     */
    public static String convert(String num, int inRadix, int outRadix) {
        return new BigInteger(num, inRadix).toString(outRadix);
    }
}
