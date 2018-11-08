package com.cuiods.cryptology.rsa.integer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyIntegerTest {

    @Test
    public void stringTest() {
        MyInteger integer = new MyInteger("10");
        System.out.println(integer);
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
        MyInteger num1 = new MyInteger("-200000000000000000000");
        MyInteger num2 = new MyInteger("-100000000000000000000");
        MyInteger result = num1.multiply(num2);
        System.out.println(result);
    }

    @Test
    public void divide() {
        MyInteger num1 = new MyInteger("6804107402578755072882");
        MyInteger num2 = new MyInteger("2537");
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
        MyInteger num = new MyInteger("2");
        System.out.println(num.pow(new MyInteger("200")));
    }
}