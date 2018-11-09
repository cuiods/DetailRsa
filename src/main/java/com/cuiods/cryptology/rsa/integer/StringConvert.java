package com.cuiods.cryptology.rsa.integer;

import java.math.BigInteger;

public class StringConvert {

    public static String convert(String num, int inRadix, int outRadix) {
        return new BigInteger(num, inRadix).toString(outRadix);
    }
}
