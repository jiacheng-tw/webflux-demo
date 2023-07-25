package com.example.webfluxdemo.design.structure.adapter;

public class V5PowerAdaptor implements V5Power{
    private final V220Power v220Power;

    public V5PowerAdaptor(V220Power v220Power) {
        this.v220Power = v220Power;
    }

    @Override
    public int provideV5Power() {
        int v220 = v220Power.provideV220Power();
        System.out.println("Adapting 220V to 5V...");
        return v220 / 44;
    }
}
