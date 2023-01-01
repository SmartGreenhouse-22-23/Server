package it.unibo.smartgh.greenhouse.adapter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.greenhouse.GreenhouseService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.*;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static it.unibo.smartgh.greenhouse.adapter.presentation.ToJSON.greenhouseToJSON;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(VertxExtension.class)
public class GreenhouseHTTPAdapterTest {
    private static final String HOST = "localhost";
    private static final int PORT = 8889;
    private static final String ID = "63af0ae025d55e9840cbc1fa";

    private final Plant plant = new PlantImpl("lemon", "is a species of small evergreen trees in the flowering plant f" +
            "amily Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.", 8.0, 35.0,
            4200.0, 130000.0, 20.0, 65.0, 30.0, 80.0);
    private final Greenhouse greenhouse = new GreenhouseImpl(plant, Modality.AUTOMATIC);

    @BeforeEach
    public void startService(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse service initializing");
        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService service = new GreenhouseService(model);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");
    }

    //todo non funziona con l'avvio del verticle
    @Test
    public void getGreenhouseTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse")
                .addQueryParam("id", ID)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(greenhouseToJSON(greenhouse).toString(), response.body());
                    testContext.completeNow();
                })));
    }
}
