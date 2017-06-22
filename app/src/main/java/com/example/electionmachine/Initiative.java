package com.example.electionmachine;

public class Initiative{
    /**
     * Класс Инициативы, голосования
     * */

   public String name;
   public String description; //Описание, цель инициативы
   public String[] variants; //Варианты(кандидаты) для голосования


   public Initiative() {

   }

   public Initiative(String name,String description,String[] variants){
       this.name = name;
	   this.description=description;
	   this.variants=variants;
   }
		   
}
