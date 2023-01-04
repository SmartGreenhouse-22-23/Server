package it.unibo.smartgh.operation.adapter;

import com.google.gson.Gson;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import it.unibo.smartgh.operation.presentation.GsonUtils;

public abstract class AbstractAdapter<API>  {

	private final Vertx vertx;
	private final API model;
	protected final Gson gson;
	
	protected AbstractAdapter(API model, Vertx vertx) {
		this.model = model;
		this.vertx = vertx;
		this.gson = GsonUtils.createGson();
	}
	
	protected Vertx getVertx() {
		return vertx;
	}

	protected API getModel() {
		return model;
	}
	
	abstract protected void setupAdapter(Promise<Void> startPromise);
	
}
