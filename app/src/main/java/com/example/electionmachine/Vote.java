package com.example.electionmachine;

import java.security.MessageDigest;

public class Vote {

    /***
     * Класс-оболочка для данных, необходимых для голосования
     */

    public String dsaSign; //Dsa подпись
    public Initiative initiative; //Инициатива, к которой голосование относится
    public String publicKey; // Публичный ключ для отправки его на сервер
    public int variant; // Вариант(Кандидат) из инициативы, за которого проголосовали

    public Vote(){

    }
    public Vote(Initiative initiative,int variant,String publicKey){
        this.initiative=initiative;
        this.variant=variant;
        this.publicKey=publicKey;
    }

    public String hashcode(){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {

        };
        String futureHash = dsaSign+initiative.toString()+publicKey+variant;
        md.update(futureHash.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
