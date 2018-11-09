package com.cuiods.cryptology.rsa.rsa;

import com.cuiods.cryptology.rsa.integer.MyInteger;
import com.cuiods.cryptology.rsa.integer.NumberUtil;
import com.cuiods.cryptology.rsa.integer.Prime;

public class RSASignature {

    private int bit = 1024;
    private static final int ASCII_BIT = 8;
    private static final int HEX_DIGIT = 16;

    public String[] generateKeys(int bit) {
        this.bit = bit;
        MyInteger E = new MyInteger("65537");
        MyInteger P = Prime.randomPrime((bit+1)/2);
        MyInteger Q = Prime.randomPrime((bit-1)/2);
        MyInteger N = P.multiply(Q);
        MyInteger fi = P.subtract(MyInteger.ONE).multiply(Q.subtract(MyInteger.ONE));
        MyInteger D = E.inverse(fi);
        return new String[]{
                N.toString(16), E.toString(16),
                D.toString(16), P.toString(16), Q.toString(16)
        };
    }

    public String encryption(String message, String e, String n) {
        int charNum = bit / ASCII_BIT;
        MyInteger E = new MyInteger(e, HEX_DIGIT);
        MyInteger N = new MyInteger(n, HEX_DIGIT);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= message.length() / charNum; i++) {
            StringBuilder tempResult = new StringBuilder();
            for (int j = i * charNum; j < i * charNum + charNum && j < message.length(); j++) {
                int temp = message.charAt(j);
                StringBuilder tempStr = new StringBuilder(Integer.toBinaryString(temp));
                while (tempStr.length() < ASCII_BIT)
                    tempStr.insert(0, "0");
                tempResult.append(tempStr);
            }
            if (tempResult.length() > 0) {
                MyInteger integer = new MyInteger(tempResult.toString(), 2);
                MyInteger resultInt = NumberUtil.speedUpMod(integer, E, N);
                StringBuilder tempStr = new StringBuilder(resultInt.toString(HEX_DIGIT));
                while (tempStr.length() % 2 != 0)
                    tempStr.insert(0, "0");
                result.append(tempStr);
            }
        }
        return result.toString();
    }

    public String decryption(String message, String d, String p, String q) {
        StringBuilder result = new StringBuilder();
        MyInteger D = new MyInteger(d, HEX_DIGIT);
        MyInteger P = new MyInteger(p, HEX_DIGIT);
        MyInteger Q = new MyInteger(q, HEX_DIGIT);
        int charNum = bit / 4;
        for (int i = 0; i <= message.length() / charNum; i++) {
            int maxLen = Math.min(i * charNum + charNum, message.length());
            if (i * charNum < message.length()) {
                String hexStr = message.substring(i * charNum, maxLen);
                MyInteger cInt = new MyInteger(hexStr, HEX_DIGIT);
                MyInteger resultInt = NumberUtil.speedUpMod(cInt, D, P, Q);
                StringBuilder resultStr = new StringBuilder(resultInt.toString(2));
                while (resultStr.length() % ASCII_BIT != 0)
                    resultStr.insert(0, "0");
                String currentStr = resultStr.toString();
                for (int j = 0; j < currentStr.length(); j+=ASCII_BIT) {
                    int c = Integer.parseInt(currentStr.substring(j, j+ASCII_BIT), 2);
                    char cRes = (char) c;
                    result.append(cRes);
                }
            }
        }
        return result.toString();
    }

}
