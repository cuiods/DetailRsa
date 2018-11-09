package com.cuiods.cryptology.rsa.integer;

import java.util.Random;

/**
 * My big integer implementation
 * @author cuiods
 */
public class MyInteger {

    /**
     * The integer is In 10^9 notation
     */
    private int[] digits;

    /**
     * -1 means less than zero, 1 means more than zero
     */
    private int sign;

    public final static int RADIX = 1000000000;

    private static final int[] ONE_NUM={1};
    private static final int[] TWO_NUM={2};
    public static final MyInteger ZERO=new MyInteger(0, new int[0]);
    public static final MyInteger ONE=new MyInteger(1, ONE_NUM);
    public static final MyInteger TWO=new MyInteger(1, TWO_NUM);
    private static final int DIGITS_PER_INT[] = {0, 0, 30, 19, 15, 13, 11,
            11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};

    /**
     * Construct integer directly
     * @param sign -1 means less than zero, 1 means more than zero
     * @param digits integers in 10^9 notation
     */
    public MyInteger(int sign, int[] digits) {
        this.sign = sign;
        this.digits = digits;
        if (isZero(digits))
            this.sign = 0;
    }

    public MyInteger(String val) {
        this(val, 10);
    }

    /**
     * Convert from string to big integer
     * @param val num string
     */
    public MyInteger(String val, int radix) {
        int cursor = 0;
        final int len = val.length();
        if (len == 0)
            throw new NumberFormatException("Zero length Integer");

        //get sign
        sign = 1;
        int index1 = val.lastIndexOf('-');
        int index2 = val.lastIndexOf('+');
        if (index1 >= 0) {
            sign = -1;
            cursor = 1;
        } else if (index2 >= 0) {
            cursor = 1;
        }

        //skip zero
        if (cursor == len)
            throw new NumberFormatException("Zero length Integer");
        while (cursor < len && Character.digit(val.charAt(cursor), radix) == 0) {
            cursor++;
        }

        if (cursor == len) {
            sign = 0;
            digits = new int[1];
            return;
        }

        //generate digits
        int digitNum = DIGITS_PER_INT[radix];
        String numberStr = val.substring(cursor);
        int numDigits = numberStr.length();
        int digitLen = (numDigits-1) / digitNum + 1;
        digits = new int[digitLen];
        int start = numDigits - (digitLen-1) * digitNum;
        for (int i = 0; i < digitLen; i++) {
            String currentStr = numberStr.substring(
                    Math.max(start+(i-1)*digitNum,0), start+i*digitNum);
            digits[i] = Integer.parseInt(currentStr, radix);
        }
    }

    /**
     * Random length integer
     * @param len digit len
     * @param random {@link Random}
     */
    public MyInteger(int len, Random random) {
        sign = 1;
        digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[i] = random.nextInt(RADIX);
        }
    }

    /**
     * To negative number
     * @return a negative {@link MyInteger}
     */
    public MyInteger negative() {
        return new MyInteger(-sign, digits);
    }

    /**
     * compare abs value
     * @param comInteger integer to compare
     * @return compare result
     */
    public int compareAbs(MyInteger comInteger) {
        return compareArray(digits, comInteger.digits);
    }

    /**
     * get abs value
     * @return abs value of current integer
     */
    public MyInteger abs() {
        return sign<0? negative(): this;
    }

    /**
     * Add a big number
     * @param integer number to be added
     * @return new integer result
     */
    public MyInteger add(MyInteger integer) {
        if (sign == 0) return integer;
        if (integer.sign == 0) return this;
        if (sign == integer.sign)
            return new MyInteger(sign, add(digits, integer.digits));
        int compare = compareAbs(integer);
        if (compare == 0)
            return ZERO;
        else if (compare > 0)
            return new MyInteger(sign, subtract(digits, integer.digits));
        else
            return new MyInteger(integer.sign, subtract(integer.digits, digits));
    }

    /**
     * Subtract a integer
     * @param integer integer
     * @return result
     */
    public MyInteger subtract(MyInteger integer) {
        if (sign == 0) return integer.negative();
        if (integer.sign == 0) return this;
        if (sign != integer.sign)
            return new MyInteger(sign, add(digits, integer.digits));
        int compare = compareAbs(integer);
        if (compare == 0)
            return ZERO;
        if (compare > 0)
            return new MyInteger(sign, subtract(digits, integer.digits));
        else
            return new MyInteger(-integer.sign, subtract(integer.digits, digits));
    }

    /**
     * Multiply an integer
     * @param integer multiplier
     * @return result
     */
    public MyInteger multiply(MyInteger integer) {
        if (sign == 0 || integer.sign == 0) return ZERO;
        int resultSign = 1;
        if (sign != integer.sign) resultSign = -1;
        int[] resultDigits = {0};
        int intLen = integer.digits.length;

        for (int i = intLen-1; i >= 0; i--) {
            if (integer.digits[i]==0) continue;
            int[] multiplyResult = multiply(digits, integer.digits[i]);
            int[] multiplyTemp = new int[multiplyResult.length + intLen -1 -i];
            System.arraycopy(multiplyResult, 0, multiplyTemp, 0, multiplyResult.length);
            resultDigits = add(resultDigits, multiplyTemp);
        }
        return new MyInteger(resultSign, resultDigits);
    }

    /**
     * Get mod number
     * @param divisor divisor
     * @return mod
     */
    public MyInteger mod(MyInteger divisor) {
        if (divisor.sign < 0)
            throw new IllegalArgumentException("Mod integer should be more than zero.");
        return divide(divisor)[1];
    }

    /**
     * Get gcd number
     * @param integer divisor
     * @return gcd
     */
    public MyInteger gcd(MyInteger integer) {
        if (isZero(integer)) return this;
        return integer.gcd(mod(integer));
    }

    /**
     * Get inverse number
     * @param modNum mod num
     * @return inverse number
     */
    public MyInteger inverse(MyInteger modNum) {
        return extEuclid(modNum)[2];
    }

    /**
     * Extend Euclid
     * @param integer {@link MyInteger}
     * @return x*integer + y*this = gcd, [0]=x, [1]=y, [2]=this^-1 mod integer
     */
    public MyInteger[] extEuclid(MyInteger integer) {
        MyInteger r0 = integer, r1 = this, s0 = ONE, s1 = ZERO, t0=ZERO, t1=ONE;
        MyInteger r,s,t;
        MyInteger reverse = ZERO;
        boolean findReverse = false;
        while (true) {
            MyInteger q = r0.divide(r1)[0];
            r = r0.subtract(q.multiply(r1));
            s = s0.subtract(q.multiply(s1));
            t = t0.subtract(q.multiply(t1));
            r0 = r1; r1 = r;
            s0 = s1; s1 = s;
            t0 = t1; t1 = t;
            if (r1.compareAbs(ONE)==0 && !findReverse) {
                reverse = t1;
                findReverse = true;
            }
            if (isZero(r1)) {
                if (!findReverse) reverse = t1;
                break;
            }
        }
        if (reverse.sign < 0) {
            reverse = reverse.add(integer);
        }

        MyInteger[] result = new MyInteger[3];
        result[0] = s0;
        result[1] = t0;
        result[2] = reverse;
        return result;
    }

    /**
     * Divide a number
     * @param divisor divisor
     * @return result[0]: quotient,  result[1]: remainder
     */
    public MyInteger[] divide(MyInteger divisor) {
        if (isZero(divisor)) throw new IllegalArgumentException("Cannot divide by zero");
        MyInteger[] result = {ZERO,ZERO};
        int compare = compareAbs(divisor);
        if (compare < 0) {
            result[1] = this;
            return result;
        }
        if (compare == 0) {
            result[0] = ONE;
            return result;
        }
        int resultSign = 1;
        if (sign != divisor.sign) resultSign = -1;

        int dividendLen = digits.length;
        int divisorLen = divisor.digits.length;
        int[] divisorVal = divisor.digits;
        int[] divideResult = new int[dividendLen-divisorLen+1];
        int[] remainderResult = new int[divisorLen];
        System.arraycopy(digits, 0, remainderResult, 0, divisorLen);
        int divideNum = 0;

        //divide process
        for (int i = 0; i <= dividendLen - divisorLen; i++) {
            if (compareArray(remainderResult,divisorVal) < 0)
                divideNum = 0;
            else {
                int left = 0;
                int right = RADIX;
                int[] tempToSubtract;
                while (left < right) {
                    int middle = (left + right) >> 1;
                    tempToSubtract = multiply(divisorVal, middle);
                    if (compareArray(tempToSubtract, remainderResult) <= 0) {
                        left = middle+1;
                    } else
                        right = middle;
                }
                divideNum = left-1;
                tempToSubtract = multiply(divisorVal, divideNum);
                remainderResult = subtract(remainderResult, tempToSubtract);
            }
            divideResult[i] = divideNum;
            if (i == dividendLen - divisorLen) break;

            if (isZero(remainderResult)) {
                remainderResult = new int[1];
                remainderResult[0] = digits[i + divisorLen];
            }
            else {
                int[] tempRemainder = new int[remainderResult.length+1];
                System.arraycopy(remainderResult, 0, tempRemainder, 0, remainderResult.length);
                tempRemainder[remainderResult.length] = digits[i + divisorLen];
                remainderResult = tempRemainder;
            }
        }
        MyInteger remainder = new MyInteger(resultSign>0?resultSign:sign, remainderResult);
        //delete zero
        int cursor = 0;
        while (cursor < divideResult.length && divideResult[cursor]==0) cursor++;
        int[] tempDivideResult = new int[divideResult.length-cursor];
        System.arraycopy(divideResult, cursor, tempDivideResult, 0, divideResult.length-cursor);
        MyInteger quotient = new MyInteger(resultSign, tempDivideResult);
        if (remainder.sign < 0) {
            remainder = remainder.add(divisor);
            quotient = quotient.subtract(ONE);
        }
        result[0] = quotient;
        result[1] = remainder;
        return result;
    }

    /**
     * Pow calculation
     * @param times multiply times
     * @return result
     */
    public MyInteger pow(MyInteger times) {
        if (isZero(times)) return ONE;
        if (times.compareAbs(ONE)==0) return this;
        MyInteger[] half = times.divide(TWO);
        MyInteger result = pow(half[0]);
        result = result.multiply(result);
        if (!isZero(half[1])) {
            result = result.multiply(this);
        }
        return result;
    }

    @Override
    public String toString() {
        return toString(10);
    }

    public String toString(int radix) {
        if (digits == null || digits.length == 0) return "0";
        StringBuilder builder = new StringBuilder(sign<0?"-":"");
        builder.append(Integer.toString(digits[0], radix));
        for (int i = 1; i < digits.length; i++) {
            StringBuilder temp = new StringBuilder(Integer.toString(digits[i], radix) + "");
            while (temp.length()<DIGITS_PER_INT[radix]) temp.insert(0, "0");
            builder.append(temp);
        }
        return builder.toString();
    }


    public boolean isZero() {
        return isZero(this);
    }

    public int getLastNumber() {
        return digits[digits.length-1];
    }

    public int length() {
        return digits.length;
    }

    /**
     * add two simple number
     * @param num1 10^9 notation of num1
     * @param num2 10^9 notation of num2
     * @return 10^9 notation of result
     */
    private int[] add(int[] num1, int[] num2) {
        int[] big = num1, small=num2;
        if (num1.length < num2.length) {
            big = num2;
            small = num1;
        }
        int[] result = new int[big.length];
        int carry = 0;
        int bigLen = big.length;
        int smallLen = small.length;
        //simple big integer add
        for (int i = bigLen-1; i > bigLen-smallLen-1; i--) {
            int sum = big[i] + small[i-(bigLen-smallLen)] + carry;
            result[i] = sum % RADIX;
            carry = sum / RADIX;
        }
        for (int i = bigLen-smallLen-1; i >= 0; i--) {
            int sum = big[i] + carry;
            result[i] = sum % RADIX;
            carry = sum / RADIX;
        }
        // process carry bit
        if (carry == 1) {
            int[] temp = new int[result.length+1];
            System.arraycopy(result, 0, temp, 1, bigLen);
            temp[0] = carry;
            result = temp;
        }
        return result;
    }

    /**
     * Subtract two simple number
     * @param big big number
     * @param small small number
     * @return result
     */
    private int[] subtract(int[] big, int[] small) {
        int[] result = new int[big.length];
        int carry = 0;
        int bigLen = big.length;
        int smallLen = small.length;
        //simple subtract
        for (int i = bigLen-1; i > bigLen-smallLen-1; i--) {
            int sub = big[i] - small[i-(bigLen-smallLen)] - carry;
            if (sub < 0) {
                result[i] = sub + RADIX;
                carry = 1;
            } else {
                result[i] = sub;
                carry = 0;
            }
        }
        for (int i = bigLen-smallLen-1; i >= 0; i--) {
            int sub = big[i] - carry;
            if (sub < 0) {
                result[i] = sub + RADIX;
                carry = 1;
            } else {
                result[i] = sub;
                carry = 0;
            }
        }
        //delete zero
        int cursor = 0;
        while (cursor < result.length && result[cursor]==0) cursor++;
        int[] temp = new int[result.length-cursor];
        System.arraycopy(result, cursor, temp, 0, result.length-cursor);
        result = temp;
        return result;
    }

    /**
     * Multiply an integer (int)
     * !!! Error when {@code sign} is -1
     * @param numbers int[]
     * @param multiplier int
     * @return result: int[]
     */
    private int[] multiply(int[] numbers, int multiplier) {
        if (multiplier == 0 || (numbers.length==1 && numbers[0]==0))
            return ZERO.digits;

        int[] result = new int[numbers.length];
        int carry = 0;
        for (int i = numbers.length - 1; i >= 0; i--) {
            long temp = (long) numbers[i] * multiplier + carry;
            result[i] = (int) (temp % RADIX);
            carry = (int) (temp / RADIX);
        }
        if (carry > 0) {
            int[] tempResult = new int[numbers.length+1];
            System.arraycopy(result, 0, tempResult, 1, result.length);
            tempResult[0] = carry;
            result = tempResult;
        }
        return result;
    }

    private boolean isZero(MyInteger integer) {
        if (isZero(integer.digits))
            return true;
        return false;
    }

    private boolean isZero(int[] integer) {
        if (integer.length==0 || (integer.length == 1 && integer[0] == 0))
            return true;
        return false;
    }

    private int compareArray(int[] array1, int[] array2) {
        if (array1.length < array2.length)
            return -1;
        else if (array1.length > array2.length)
            return 1;
        else {
            for (int i = 0; i < array1.length; i++) {
                if (array1[i] < array2[i])
                    return -1;
                else if (array1[i] > array2[i])
                    return 1;
            }
        }
        return 0;
    }

}
