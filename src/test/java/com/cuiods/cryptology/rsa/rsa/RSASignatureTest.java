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
        String c = signature.encryption("1211413413sfqef24g54g25g2gh22", keys[1], keys[0], 768/2);
        System.out.println(c);
        String res = signature.decryption(c, keys[2], keys[3], keys[4], 768/2);
        System.out.println(res);
    }

    @Test
    public void decryption() {
    }
}