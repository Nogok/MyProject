package com.example.electionmachine;

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
}
