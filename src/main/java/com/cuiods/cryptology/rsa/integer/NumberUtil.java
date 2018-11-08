package com.cuiods.cryptology.rsa.integer;

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
     * Calculate num^d mod n
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
        numP = numP.pow(dP).mod(p);
        numQ = numQ.pow(dQ).mod(q);
        MyInteger[] x = {numP, numQ};
        MyInteger[] m = {p,q};
        return CrtResult(m, x);
    }
}
