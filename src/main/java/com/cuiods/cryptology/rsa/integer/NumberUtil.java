package com.cuiods.cryptology.rsa.integer;

import java.security.SecureRandom;

/**
 * Speed up mod implementation
 */
public class NumberUtil {

    /**
     * Calculate CRT result
     * @param m mi
     * @param x xi
     * @return result
     */
    public static MyInteger CrtResult(MyInteger[] m, MyInteger[] x) {
        assert m.length == x.length;
        int len = m.length;
        MyInteger M = MyInteger.ONE;
        for (MyInteger aM : m) {
            M = M.multiply(aM);
        }
        MyInteger[] MArray = new MyInteger[len];
        MyInteger[] NArray = new MyInteger[len];
        MyInteger result = MyInteger.ZERO;

        for (int i = 0; i < len; i++) {
            MArray[i] = M.divide(m[i])[0];
            NArray[i] = MArray[i].inverse(m[i]);
            result = result.add(x[i].multiply(MArray[i]).multiply(NArray[i]));
        }
        return result.mod(M);
    }

    /**
     * Calculate num^d mod p*q
     * @param num num
     * @param d d
     * @param p n = p * q
     * @param q n = p * q
     * @return num^d mod n
     */
    public static MyInteger speedUpMod(MyInteger num, MyInteger d, MyInteger p, MyInteger q) {
        MyInteger numP = num.mod(p);
        MyInteger numQ = num.mod(q);
        MyInteger dP = d.mod(p.subtract(MyInteger.ONE));
        MyInteger dQ = d.mod(q.subtract(MyInteger.ONE));
        numP = speedUpMod(numP, dP, p);
        numQ = speedUpMod(numQ, dQ, q);
        MyInteger[] x = {numP, numQ};
        MyInteger[] m = {p,q};
        return CrtResult(m, x);
    }

    /**
     * Calculate num^d mod n
     * @param num num
     * @param d d
     * @param n n
     * @return num^d mod n
     */
    public static MyInteger speedUpMod(MyInteger num, MyInteger d, MyInteger n) {
        if (d.isZero()) return MyInteger.ONE;
        if (d.compareAbs(MyInteger.ONE)==0) return num.mod(n);
        num = num.mod(n);
        MyInteger[] oddTest = d.divide(MyInteger.TWO);
        MyInteger temp = speedUpMod(num, oddTest[0], n);
        MyInteger result = temp.multiply(temp).mod(n);
        if (!oddTest[1].isZero()) {
            result = result.multiply(num).mod(n);
        }
        return result;
    }
}
