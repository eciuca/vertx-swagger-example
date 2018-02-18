package com.github.eciuca.vertx.swagger;

import io.swagger.annotations.ApiOperation;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class GreetingsResource {

    @ApiOperation(value = "sayHello")
    public void sayHelloHandler(Message<JsonObject> message) {
        Greeting greeting = new Greeting("Hello, " + message.body().getString("name"));

        message.reply(new JsonObject(Json.encode(greeting)).encodePrettily());

    }
}
