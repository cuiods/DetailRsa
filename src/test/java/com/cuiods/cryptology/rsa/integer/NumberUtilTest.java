package com.cuiods.cryptology.rsa.integer;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilTest {

    @Test
    public void crtResult() {
        MyInteger[] m = {new MyInteger("7"), new MyInteger("11"), new MyInteger("13")};
        MyInteger[] x = {new MyInteger("5"), new MyInteger("3"), new MyInteger("10")};
        System.out.println(NumberUtil.CrtResult(m,x));
    }

    @Test
    public void speedUpMod() {
        MyInteger num = new MyInteger("23911231");
        MyInteger p = new MyInteger("43");
        MyInteger q = new MyInteger("59");
        MyInteger d = new MyInteger("937121");
        System.out.println(NumberUtil.speedUpMod(num, d, p, q));
    }

    @Test
    public void speedUpMod1() {
        MyInteger num = new MyInteger("23911231");
        MyInteger p = new MyInteger("43");
        MyInteger q = new MyInteger("59");
        MyInteger d = new MyInteger("937121");
        System.out.println(NumberUtil.speedUpMod(num, d, p.multiply(q)));
    }
}