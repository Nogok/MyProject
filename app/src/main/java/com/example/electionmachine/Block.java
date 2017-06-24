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
    long nonce = 0; // добавка для генерации
    private String previousHash;
    private  long timestamp; //Date and time of operation
    private String voteHash = ""; // Голоса в блоке
    private ArrayList<Vote> votes;
    private String goal;

    Block(){

    }

    // Конструктор для остальных блоков
    Block(ArrayList<Vote> votes, Block previousBlock,String goal){
        this.votes = votes;
        this.previousHash = previousBlock.hash;
        this.index = previousBlock.index + 1;
        this.hash = hashcode();
        this.goal = goal;
        timestamp = System.currentTimeMillis();
       createVoteHash(votes);
    }

    // hash function
    String hashcode(){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String futureHash = voteHash+index+timestamp+nonce+previousHash+goal;
            md.update(futureHash.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b:byteData) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

    String getHash() {
        return hash;
    }
    private void createVoteHash(ArrayList<Vote> votes){
        for (int i = 0; i < this.votes.size(); i++){
            voteHash += votes.get(i).hashcode();
        }
    }


}
