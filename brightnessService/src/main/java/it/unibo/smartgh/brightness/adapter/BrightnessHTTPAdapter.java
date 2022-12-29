package it.unibo.smartgh.brightness.adapter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.brightness.api.BrightnessAPI;

public class BrightnessHTTPAdapter extends AbstractAdapter<BrightnessAPI> {

    private static final String BASE_PATH = "/brightness";
    private static final String HISTORY_PATH = "/brightness/history/:howMany";

    private HttpServer server;
    private Router router;
    private String host;
    private String port;

    public BrightnessHTTPAdapter(BrightnessAPI model, Vertx vertx) {
        super(model, vertx);
    }

    @Override
    protected void setupAdapter(Promise<Void> startPromise) {

    }
}
