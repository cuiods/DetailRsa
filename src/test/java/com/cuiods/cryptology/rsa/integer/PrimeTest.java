package com.cuiods.cryptology.rsa.integer;

import org.junit.Test;

import static org.junit.Assert.*;

public class PrimeTest {

    @Test
    public void fermatTest() {
        System.out.println(Prime.fermatTest(new MyInteger("1"),3));
    }

    @Test
    public void millerRabinTest() {
        String[] primes = { "1", "3", "3613", "7297",
                "226673591177742970257407", "2932031007403" };
        String[] nonPrimes = { "3341", "2932021007403",
                "226673591177742970257405" };
        int k = 3;
        for (String p : primes)
            System.out.println(Prime.millerRabinTest(new MyInteger(p), k));
        for (String n : nonPrimes)
            System.out.println(Prime.millerRabinTest(new MyInteger(n), k));
    }

    @Test
    public void isPrime() {
        String[] primes = { "3", "3613", "7297",
                "226673591177742970257407", "2932031007403" };
        String[] nonPrimes = {"1", "3341", "2932021007403",
                "226673591177742970257405" };
        for (String p : primes)
            System.out.println(Prime.isPrime(new MyInteger(p), true));
        for (String n : nonPrimes)
            System.out.println(Prime.isPrime(new MyInteger(n), true));
    }

    @Test
    public void randomPrime() {
        System.out.println(Prime.randomPrime(384));
        System.out.println(Prime.randomPrime(384));
    }
}