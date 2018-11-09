package com.cuiods.cryptology.rsa.rsa;

import org.junit.Test;

import static org.junit.Assert.*;

public class RSASignatureTest {

    @Test
    public void generateKeys() {
        new RSASignature().generateKeys(768);
    }

    @Test
    public void encryption() {
        RSASignature signature = new RSASignature();
        String[] keys = signature.generateKeys(768);
        String c = signature.encryption("hello!", keys[1], keys[0]);
        System.out.println(c);
        String res = signature.decryption(c, keys[2], keys[3], keys[4]);
        System.out.println(res);
    }

    @Test
    public void decryption() {
    }
}