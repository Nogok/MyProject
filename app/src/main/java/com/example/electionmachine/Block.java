package com.example.electionmachine;

import java.security.MessageDigest;
import java.util.ArrayList;


class Block  {
    /**
     * Класс Block
     * Предназначен для создания заготовки блока перед его генерацией. После нахождения
     * необходимого nonce отправляется на сервер, где любой может проверить, действительно ли хэш этого блока удовлетворяет
     * условию blockhash < goal
     **/
    // Variables
    private String hash;
    private int index = 0; //Index of operation
    public long nonce = 0; // добавка для генерации
    private String previousHash;
    private  long timestamp; //Date and time of operation
    private String voteHash = ""; // Голоса в блоке
    public ArrayList<Vote> votes;
    public String goal;

    public Block(){

    }

    // Конструктор для остальных блоков
    public Block(ArrayList<Vote> votes, Block previousBlock,String goal){
        this.votes = votes;
        this.previousHash = previousBlock.hash;
        this.index = previousBlock.index + 1;
        this.hash = hashcode();
        this.goal = goal;
        timestamp = System.currentTimeMillis();
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
            e.printStackTrace();
        }
        String futureHash = voteHash+index+timestamp+nonce+previousHash+goal;
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


}
