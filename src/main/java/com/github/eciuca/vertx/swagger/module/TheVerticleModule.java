package com.github.eciuca.vertx.swagger.module;

import com.github.eciuca.vertx.swagger.GreetingsResource;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class TheVerticleModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    public GreetingsResource providesGreetingsResource() {
        return new GreetingsResource();
    }
}
