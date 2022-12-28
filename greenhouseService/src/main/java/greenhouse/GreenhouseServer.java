package greenhouse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
public class GreenhouseServer extends AbstractVerticle {
    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle(new MongoVerticle());
        vertx.deployVerticle(new GreenhouseServer());
    }

    @Override
    public void start() throws Exception {
        //TODO
        System.out.println("ciao");
    }
}
