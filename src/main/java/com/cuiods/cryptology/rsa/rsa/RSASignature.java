package com.cuiods.cryptology.rsa.rsa;

import com.cuiods.cryptology.rsa.integer.*;

/**
 * RSA algorithm
 * @author cuiods
 */
public class RSASignature {

    private int bit = 0;
    private static final int ASCII_BIT = 8;
    private static final int MAX_ASCII = 256;

    /**
     * Generate RSA keys
     * @param bit RSA bit length (768, 1024, 2048)
     * @return 0:N  1:E  2:D   3:P  4:Q
     */
    public String[] generateKeys(int bit) {
        this.bit = bit/2;
        MyInteger E = new MyInteger("65537");
        // Random P, Q
        MyInteger P = Prime.randomPrime((bit+1)/2);
        MyInteger Q = Prime.randomPrime((bit-1)/2);
        MyInteger N = P.multiply(Q);
        MyInteger fi = P.subtract(MyInteger.ONE).multiply(Q.subtract(MyInteger.ONE));
        // Confirm E
        while (E.gcd(fi).compareAbs(MyInteger.ONE)!=0) {
            E = E.add(MyInteger.TWO);
        }
        // Get D
        MyInteger D = E.inverse(fi);
        return new String[]{
                N.toString(), E.toString(),
                D.toString(), P.toString(), Q.toString()
        };
    }

    /**
     * Encryption
     * @param message message
     * @param e e in DECIMAL representation
     * @param n n in DECIMAL representation
     * @return message
     */
    public String encryption(String message, String e, String n) {
        int charNum = bit / ASCII_BIT;
        MyInteger E = new MyInteger(e);
        MyInteger N = new MyInteger(n);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= message.length() / charNum; i++) {
            StringBuilder tempResult = new StringBuilder();
            for (int j = i * charNum; j < i * charNum + charNum && j < message.length(); j++) {
                int temp = message.charAt(j);
                if (temp > MAX_ASCII)
                    temp = '?';
                StringBuilder tempStr = new StringBuilder(Integer.toBinaryString(temp));
                while (tempStr.length() < ASCII_BIT)
                    tempStr.insert(0, "0");
                tempResult.append(tempStr);
            }
            if (tempResult.length() > 0) {
                MyInteger integer = new MyInteger(StringConvert.convert(tempResult.toString(),2,10));
                MyInteger resultInt = SpeedUp.speedUpMod(integer, E, N);
                StringBuilder tempStr = new StringBuilder(StringConvert.convert(resultInt.toString(),10,16));
                while (tempStr.length() % (bit / 2) != 0)
                    tempStr.insert(0, "0");
                result.append(tempStr);
            }
        }
        return result.toString();
    }

    /**
     * Decryption
     * @param message message
     * @param d d in DECIMAL representation
     * @param p p in DECIMAL representation
     * @param q q in DECIMAL representation
     * @return origin message
     */
    public String decryption(String message, String d, String p, String q) {
        StringBuilder result = new StringBuilder();
        MyInteger D = new MyInteger(d);
        MyInteger P = new MyInteger(p);
        MyInteger Q = new MyInteger(q);
        int charNum = bit / 2;
        for (int i = 0; i <= message.length() / charNum; i++) {
            int maxLen = Math.min(i * charNum + charNum, message.length());
            if (i * charNum < message.length()) {
                String hexStr = message.substring(i * charNum, maxLen);
                MyInteger cInt = new MyInteger(StringConvert.convert(hexStr,16,10));
                MyInteger resultInt = SpeedUp.speedUpMod(cInt, D, P, Q);
                StringBuilder resultStr = new StringBuilder(StringConvert.convert(resultInt.toString(),10,2));
                while (resultStr.length() % ASCII_BIT != 0)
                    resultStr.insert(0, "0");
                String currentStr = resultStr.toString();
                for (int j = 0; j < currentStr.length(); j+=ASCII_BIT) {
                    int c = Integer.parseInt(currentStr.substring(j, j+ASCII_BIT), 2);
                    char cRes = (char) c;
                    result.append(cRes);
                }
            } else break;
        }
        return result.toString();
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }
}
