package com.github.eciuca.vertx.swagger;

import com.github.phiz71.vertx.swagger.router.OperationIdServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import com.google.inject.Injector;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class TheVerticle extends AbstractVerticle {

    public static final String SWAGGER_YML = "swagger.yml";
    public static final List<Class> RESOURCES = asList(
            GreetingsResource.class,
            JokesResource.class
    );

    private final Injector injector;

    @Inject
    public TheVerticle(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.fileSystem().readFile(SWAGGER_YML, readFile -> {
            if (readFile.succeeded()) {

                Router swaggerRouter = configureSwaggerRouter(readFile);
                loadResourceHandlers(RESOURCES);

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

    private void loadResourceHandlers(List<Class> resources) {
        resources.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(ApiOperation.class))
                .forEach(method -> {
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);

                    vertx.eventBus()
                            .<JsonObject>consumer(apiOperation.value())
                            .handler(handler -> {
                                try {
                                    method.invoke(injector.getInstance(method.getDeclaringClass()), handler);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            });

                });
    }
}
