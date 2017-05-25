package com.example.electionmachine;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MyDSA {

    private BigInteger x = BigInteger.valueOf(7) ; //private key
    public BigInteger y ; //public key
    public BigInteger p,q,g; //numbs
    BigInteger messageHash;
    BigInteger r,s;
    private final static BigInteger one = BigInteger.ONE;

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getS() {
        return s;
    }




    public MyDSA(String message) {
        messageHash = BigInteger.valueOf(message.hashCode());
        int N = Integer.toBinaryString(message.hashCode()).length();
        System.out.println(N);
        q = BigInteger.valueOf(53);

        createP();
        createG();
        createPublicKey();
        signature();
    }

    private void createPublicKey(){
        this.y = g.modPow(x, p) ;
    }
    private void createP() {

        int q = this.q.intValue();
        int p = 2*q +1 ;
        if ((p-1) % q == 0){
            this.p = BigInteger.valueOf(p);
            return;
        }
        createP();

    }

    private void createG() {
        int h = 2;
        if (h > p.intValue()) createG();
        this.g = BigInteger.valueOf(h).modPow((p.subtract(one)).divide(q), p);
        if (g.intValue() != 1) return;
        else createG();

    }
    private void signature() {
        BigInteger k = BigInteger.valueOf(3);
        if (k.intValue() >= q.intValue()) signature();
        BigInteger r = g.modPow(k, p).mod(q);

        if (r.intValue() == 0) signature();
        BigInteger z = k.modPow(q.subtract(BigInteger.valueOf(2)), q);
        BigInteger s = z.multiply(messageHash.add(x.multiply(r))).mod(q);
        if (s.intValue() == 0) signature();
        this.r = r;
        this.s = s;
    }
//    private boolean verifySignature() {
//        BigInteger w = s.modInverse(q);
//        BigInteger U = messageHash.multiply(w);
//        U = U.mod(q);
//        BigInteger u = r.multiply(w);
//        u = u.mod(q);
//
//        U = g.pow(U.intValue());
//        u = y.pow(u.intValue());
//
//        BigInteger v = U.multiply(u);
//        v = v.mod(p);
//        v = v.mod(q);
//        return v.intValue() == r.intValue();
//
//    }

}