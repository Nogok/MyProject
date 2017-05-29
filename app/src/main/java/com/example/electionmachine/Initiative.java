package com.example.electionmachine;

public class Initiative{
    /**
     * Класс Инициативы, голосования
     * */

   public String description; //Описание, цель инициативы
   public String[] variants; //Варианты(кандидаты) для голосования


   public Initiative() {

   }

   public Initiative(String description,String[] variants){
	   this.description=description;
	   this.variants=variants;
   }
		   
}
