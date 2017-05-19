package com.example.electionmachine;
public class Elector {

    String name;
    int age;

    public Elector(String name,	int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {

        return String.format("%30s|%15d",name,
                age);
    }

}
