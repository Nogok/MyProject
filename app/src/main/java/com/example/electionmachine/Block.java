package com.example.electionmachine;

import java.security.MessageDigest;

import java.util.Date;
import java.util.Stack;

public class Block  {

    // Variables
    private int index = 0; //Index of operation
    private final Date timestamp; //Date and time of operation
    private String data; // Data in Block
    private String hash, previousHash;
    private Block previousBlock = null;


    // Constructor for the FIRST Block
    public Block(String data){
        this.data = data;
        this.previousHash = "0";
        index = 1;
        this.hash = hashcode();
        timestamp = new Date();
    }

    // Constructor for other Blocks
    public Block(String data, Block previousBlock){
        this.data = data;
        this.previousHash = previousBlock.hash;
        this.index = previousBlock.index + 1;
        this.hash = hashcode();
        timestamp = new Date();
        this.previousBlock = previousBlock;

    }

    // hash function
    public String hashcode(){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {

        };
        String futureHash = data+index+timestamp;
        md.update(futureHash.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static boolean blockValidity(Block a, Stack<Block> chain){
        Block b = chain.firstElement();
        if ((a.index == b.index+1) && (a.previousHash.equals(b.hash)) ) return true;
        else return false;
    }

    // TODO Cycle
    //  Probably useless
//    public static boolean checkChain(Stack<Block> originChain, Stack<Block> userChain){
//
//
//
//        Block a = originChain.firstElement();
//        Block b = userChain.firstElement();
//        boolean f = false;
//
//        if ((a.index == b.index) && (a.hash.equals(b.hash)) &&
//                (a.previousHash.equals(b.previousHash)) && (a.timestamp.equals(b.timestamp)) && (a.data.equals(b.data)))
//            f = true;
//
//        return f;
//
//    }



    public int getIndex() {
        return index;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
    public Block getPreviousBlock() {
        return previousBlock;
    }

}
