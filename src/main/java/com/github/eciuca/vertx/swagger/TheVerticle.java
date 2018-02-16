package com.github.eciuca.vertx.swagger;

import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.nio.charset.Charset;

public class TheVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.fileSystem().readFile("swagger.yml", readFile -> {
            if (readFile.succeeded()) {
                System.out.println("File read successfully");

                Router swaggerRouter = configureSwaggerRouter(readFile);

                vertx.eventBus().<JsonObject>consumer("sayHello").handler(new GreetingsResource()::sayHelloHandler);

                createHttpServer(startFuture, swaggerRouter);
            } else {
                System.out.println("Could not read file");
            }
        });
    }

    private Router configureSwaggerRouter(AsyncResult<Buffer> readFile) {
        Swagger swagger = new SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")));
        return SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, vertx.eventBus(), new OperationIdServiceIdResolver());
    }


    private void createHttpServer(Future<Void> startFuture, Router swaggerRouter) {
        vertx.createHttpServer()
                .requestHandler(swaggerRouter::accept)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }
}
