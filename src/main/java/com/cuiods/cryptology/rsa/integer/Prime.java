package com.cuiods.cryptology.rsa.integer;


public class Prime {

    private static final MyInteger THREE = new MyInteger("3");
    private static final MyInteger FOUR = new MyInteger("4");

    private static final MyInteger[] KNOWN_PRIME = {
            MyInteger.TWO, new MyInteger("3"), new MyInteger("5"), new MyInteger("7"), new MyInteger("11"),
            new MyInteger("13"), new MyInteger("17"), new MyInteger("19"), new MyInteger("23"),
            new MyInteger("29"), new MyInteger("31"), new MyInteger("37"), new MyInteger("41"),
            new MyInteger("43"), new MyInteger("47"), new MyInteger("53"), new MyInteger("59"),
            new MyInteger("61"), new MyInteger("67"), new MyInteger("73"), new MyInteger("79"),
            new MyInteger("83"), new MyInteger("89"), new MyInteger("97"), new MyInteger("101")
    };

    /**
     * Get a n bit prime {@link MyInteger}
     * @param bit bit number
     * @return prime integer
     */
    public static MyInteger randomPrime(int bit) {
        MyInteger integer = RandomUtil.randomOdd(bit);
        boolean big = ((bit + 1)/2)>=95;
        while (!isPrime(integer, big)) {
            integer = integer.add(MyInteger.TWO);
        }
        return integer;
    }

    /**
     * Prime judgement algorithm
     * step1: preselection by testing divisions by small prime numbers
     * step2: apply a Fermat test
     * step3: apply a certain number (3) of Miller-Rabin tests
     * @param integer integer to be judged
     * @return whether a prime
     */
    public static boolean isPrime(MyInteger integer, boolean big) {
        if (big) {
            int preTest = preSelection(integer);
            if (preTest > 0)
                return true;
            else if (preTest < 0)
                return false;
        }
        if (!fermatTest(integer, 1)) {
            return false;
        }
        return millerRabinTest(integer, 3);
    }

    static int preSelection(MyInteger integer) {
        if ((integer.compareAbs(MyInteger.ONE) <= 0) || integer.compareAbs(FOUR)==0) return -1;
        if (integer.compareAbs(THREE) <= 0) return 1;
        for (MyInteger knowPrime: KNOWN_PRIME) {
            MyInteger[] divRes = integer.divide(knowPrime);
            if (divRes[1].isZero()) {
                if (divRes[0].compareAbs(MyInteger.ONE)==0)
                    return 1;
                else
                    return -1;
            }
        }
        return 0;
    }

    static boolean fermatTest(MyInteger n, int k) {
        if ((n.compareAbs(MyInteger.ONE) <= 0) || n.compareAbs(FOUR)==0) return false;
        if (n.compareAbs(THREE) <= 0) return true;
        while (k > 0) {
            int a = 2;
            if (NumberUtil.speedUpMod(new MyInteger(a+""),
                    n.subtract(MyInteger.ONE), n).compareAbs(MyInteger.ONE)!=0)
                return false;
            k--;
        }
        return true;
    }

    static boolean millerRabinTest(MyInteger n, int k) {
        if ((n.compareAbs(MyInteger.ONE) <= 0)) return false;
        if (n.compareAbs(THREE) <= 0) return true;
        int s = 0;
        MyInteger n_1 = n.subtract(MyInteger.ONE);
        MyInteger d = n_1;
        while (d.mod(MyInteger.TWO).isZero()) {
            s++;
            d = d.divide(MyInteger.TWO)[0];
        }
        for (int i = 0; i < k; i++) {
            MyInteger a = RandomUtil.randomInteger(MyInteger.TWO, n_1);
            MyInteger x = NumberUtil.speedUpMod(a,d,n);
            if (x.compareAbs(MyInteger.ONE)==0 || x.compareAbs(n.subtract(MyInteger.ONE))==0)
                continue;
            int r = 0;
            for (; r < s; r++) {
                x = x.multiply(x).mod(n);
                if (x.compareAbs(MyInteger.ONE)==0) return false;
                if (x.compareAbs(n_1)==0) break;
            }
            if (r == s) return false;
        }
        return true;
    }
}
