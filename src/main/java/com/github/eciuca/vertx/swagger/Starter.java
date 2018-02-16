package com.github.eciuca.vertx.swagger;

import io.vertx.core.Launcher;

public class Starter {

    public static void main(String[] args) {
        new Launcher().dispatch(new String[] {
                "run", TheVerticle.class.getName()
        });
    }
}
