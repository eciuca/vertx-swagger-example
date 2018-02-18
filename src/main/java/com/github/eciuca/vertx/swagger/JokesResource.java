package com.github.eciuca.vertx.swagger;

import io.swagger.annotations.ApiOperation;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class JokesResource {

    @ApiOperation(value = "sayAJoke")
    public void sayAJoke(Message<JsonObject> message) {
        Joke joke = new Joke("Q: Will will smith smith will smith? A: Will smith will smith will smith!", true);

        message.reply(new JsonObject(Json.encode(joke)).encodePrettily());

    }
}
