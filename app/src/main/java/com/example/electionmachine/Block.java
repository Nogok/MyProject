package com.example.electionmachine;

import java.security.MessageDigest;

import java.util.Date;
import java.util.List;
import java.util.Stack;

public class Block  {
    /**
     * Класс Block
     * Предназначен для создания заготовки блока перед его генерацией. После нахождения
     * необходимого nonce отправляется на сервер, где любой может проверить, действительно ли хэш этого блока удовлетворяет
     * условию blockhash < goal
     **/
    // Variables
    private int index = 0; //Index of operation
    private final Date timestamp; //Date and time of operation
    private String voteHash = ""; // Голоса в блоке
    public List<Vote> votes;
    private String hash, previousHash;
    private Block previousBlock = null;
    public long nonce = 0; // добавка для генерации


    public Block(){
        timestamp = new Date();
        voteHash = "0";
    }

    // Конструктор для остальных блоков
    public Block(List<Vote> votes, Block previousBlock){
        this.votes = votes;
        this.previousHash = previousBlock.hash;
        this.index = previousBlock.index + 1;
        this.hash = hashcode();
        timestamp = new Date();
        this.previousBlock = previousBlock;
        for (int i = 0; i < votes.size(); i++){
            voteHash += votes.get(i).hashcode();
        }

    }

    // hash function
    public String hashcode(){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {

        }
        String futureHash = voteHash+index+timestamp+nonce;
        md.update(futureHash.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        hash = sb.toString();
        return hash;
    }


    public int getIndex() {
        return index;
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
