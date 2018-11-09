package com.cuiods.cryptology.rsa.integer;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.*;

public class MyIntegerTest {

    @Test
    public void stringTest() {
        MyInteger integer = new MyInteger("1341325135123514353535435353535435434142142131414");
        BigInteger integer1 = new BigInteger("1341325135123514353535435353535435434142142131414");
        System.out.println(integer.toString(10));
        System.out.println(integer1.toString(10));
    }

    @Test
    public void add() {
        MyInteger num1 = new MyInteger("-10000000000000000000000000");
        MyInteger num2 = new MyInteger("20000000000000000000000000");
        System.out.println(num1.add(num2));
    }

    @Test
    public void subtract() {
        MyInteger num1 = new MyInteger("20000000000000000000000000000000000000000");
        MyInteger num2 = new MyInteger("-10000000000000000000000000000000000000000");
        System.out.println(num1.subtract(num2));
    }

    @Test
    public void multiply() {
        MyInteger num1 = new MyInteger("15169557");
        MyInteger num2 = new MyInteger("7469390");
        MyInteger result = num1.multiply(num2);
        System.out.println(result);
    }

    @Test
    public void divide() {
        MyInteger num1 = new MyInteger("12413413415421534513451451");
        MyInteger num2 = new MyInteger("13153634636");
        MyInteger[] result = num1.divide(num2);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    @Test
    public void extEuclid() {
        MyInteger num1 = new MyInteger("12313413");
        MyInteger num2 = new MyInteger("4346437");
        System.out.println(num1.extEuclid(num2)[2]);
    }

    @Test
    public void pow() {
        MyInteger num = new MyInteger("211");
        System.out.println(num.pow(new MyInteger("200111")));
    }
}