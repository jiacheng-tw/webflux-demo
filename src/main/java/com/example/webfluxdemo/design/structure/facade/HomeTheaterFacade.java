package com.example.webfluxdemo.design.structure.facade;

public class HomeTheaterFacade {
    private Light light;
    private Player player;
    private Projector projector;

    public HomeTheaterFacade(Light light, Player player, Projector projector) {
        this.light = light;
        this.player = player;
        this.projector = projector;
    }

    public void watchMovie() {
        light.turnOff();
        projector.turnOn();
        player.turnOn();
    }

    public void stopMovie() {
        light.turnOn();
        projector.turnOff();
        player.turnOff();
    }
}
