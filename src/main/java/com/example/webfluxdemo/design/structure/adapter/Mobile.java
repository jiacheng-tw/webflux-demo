package com.example.webfluxdemo.design.structure.adapter;

public class Mobile {
    public void charge (V5Power v5Power) {
        int power = v5Power.provideV5Power();
        System.out.printf("The mobile is charging with %dV...%n", power);
    }
}
