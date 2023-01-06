package it.unibo.smartgh.greenhouse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.greenhouse.adapter.GreenhouseHTTPAdapter;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;

import static it.unibo.smartgh.greenhouse.Logger.log;
/**
 * Class that represent the Service, it extends the abstract class {@link AbstractVerticle} of Vertx.
 */
public class GreenhouseService extends AbstractVerticle {
    private final static String host = "localhost";
    private final static int PORT = 8889;
    private final GreenhouseAPI model;
    private GreenhouseHTTPAdapter httpAdapter;
    /**
     * Constructor of humidity service.
     * @param model the greenhouse model.
     */
    public GreenhouseService(GreenhouseAPI model) {
        this.model = model;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        installAdapters(startPromise);
        startPromise.complete();
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            /*
             * Installing only the HTTP adapter.
             */
            this.httpAdapter = new GreenhouseHTTPAdapter(model, host, PORT, this.getVertx());
            Promise<Void> p = Promise.promise();
            httpAdapter.setupAdapter();
            Future<Void> fut = p.future();
            fut.onSuccess(res -> {
                startPromise.complete();
                log("HTTP adapter installed.");
            }).onFailure(f -> {
                startPromise.fail("HTTP adapter installation failed.");
                log("HTTP adapter not installed.");
            });
        } catch (Exception ex) {
            startPromise.fail("HTTP adapter installation failed.");
            log("HTTP adapter installation failed.");
        }
    }
}
