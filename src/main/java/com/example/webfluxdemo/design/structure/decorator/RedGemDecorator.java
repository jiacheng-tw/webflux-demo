package com.example.webfluxdemo.design.structure.decorator;

public class RedGemDecorator implements EquipmentDecorator{
    private final Equipment equipment;

    public RedGemDecorator(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public int calculateAttack() {
        return equipment.calculateAttack() + 10;
    }

    @Override
    public String description() {
        return equipment.description() + " + 红宝石";
    }
}
