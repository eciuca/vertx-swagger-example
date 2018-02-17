package com.github.eciuca.vertx.swagger;

import io.vertx.core.Launcher;

public class Starter {

    public static void main(String[] args) {
        new Launcher().dispatch(new String[] {
                "run", "java-guice:" + TheVerticle.class.getName(),
                "-conf", "src/application.yml"
        });
    }
}
