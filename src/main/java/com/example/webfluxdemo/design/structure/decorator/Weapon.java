package com.example.webfluxdemo.design.structure.decorator;

public class Weapon implements Equipment{

    @Override
    public int calculateAttack() {
        return 10;
    }

    @Override
    public String description() {
        return "武器";
    }
}
