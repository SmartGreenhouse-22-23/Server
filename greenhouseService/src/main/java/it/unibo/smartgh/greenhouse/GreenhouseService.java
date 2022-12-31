package it.unibo.smartgh.greenhouse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.greenhouse.adapter.GreenhouseHTTPAdapter;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;

import static it.unibo.smartgh.greenhouse.Logger.log;

public class GreenhouseService extends AbstractVerticle {
    private final static String host = "localhost";
    private final static int PORT = 8889;
    private final GreenhouseAPI model;
    private GreenhouseHTTPAdapter httpAdapter;
    public GreenhouseService(GreenhouseAPI model) {
        this.model = model;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        installAdapters(startPromise);
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            /*
             * Installing only the HTTP adapter.
             */
            this.httpAdapter = new GreenhouseHTTPAdapter(model, "localhost", PORT, this.getVertx());
            Promise<Void> p = Promise.promise();
            httpAdapter.setupAdapter(p);
            Future<Void> fut = p.future();
            fut.onSuccess(res -> {
                log("HTTP adapter installed.");
            }).onFailure(f -> {
                log("HTTP adapter not installed.");
            });
        } catch (Exception ex) {
            log("HTTP adapter installation failed.");
        }
    }
}
