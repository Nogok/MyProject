package com.example.electionmachine;

class Initiative{
    /**
     * Класс Инициативы, голосования
     * */


   String description; //Описание, цель инициативы
   String name; // Название инициативы
   String[] variants; //Варианты(кандидаты) для голосования

   Initiative() {

   }

   Initiative(String description,String name,String[] variants){
	   this.description=description;
       this.name = name;
	   this.variants=variants;
   }
		   
}
