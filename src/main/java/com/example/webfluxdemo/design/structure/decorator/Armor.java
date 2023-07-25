package com.example.webfluxdemo.design.structure.decorator;

public class Armor implements Equipment{

    @Override
    public int calculateAttack() {
        return 5;
    }

    @Override
    public String description() {
        return "盔甲";
    }
}
