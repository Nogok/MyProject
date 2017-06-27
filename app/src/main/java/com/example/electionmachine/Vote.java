package com.example.electionmachine;

import java.security.MessageDigest;

class Vote {

    /***
     * Класс-оболочка для данных, необходимых для голосования
     */

    String dsaSign; //Dsa подпись
    private Initiative initiative; //Инициатива, к которой голосование относится
    private String publicKey; // Публичный ключ для отправки его на сервер
    int variant; // Вариант(Кандидат) из инициативы, за которого проголосовали

    Vote(){

    }
    Vote(Initiative initiative,int variant,String publicKey){
        this.initiative=initiative;
        this.variant=variant;
        this.publicKey=publicKey;
    }

    String hashcode(){
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String futureHash = dsaSign+initiative.toString()+publicKey+variant;
            md.update(futureHash.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b:byteData) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
