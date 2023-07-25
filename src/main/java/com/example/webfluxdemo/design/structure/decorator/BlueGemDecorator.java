package com.example.webfluxdemo.design.structure.decorator;

public class BlueGemDecorator implements EquipmentDecorator{
    private final Equipment equipment;

    public BlueGemDecorator(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public int calculateAttack() {
        return equipment.calculateAttack() + 5;
    }

    @Override
    public String description() {
        return equipment.description() + " + 蓝宝石";
    }
}
