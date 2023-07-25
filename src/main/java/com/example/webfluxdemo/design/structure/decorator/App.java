package com.example.webfluxdemo.design.structure.decorator;

public class App {
    public static void main(String[] args) {
        Equipment weapon = new RedGemDecorator(new BlueGemDecorator(new Weapon()));
        System.out.println(weapon.description() + " : " + weapon.calculateAttack());

        Equipment armor = new BlueGemDecorator(new RedGemDecorator(new Armor()));
        System.out.println(armor.description() + " : " + armor.calculateAttack());
    }
}
