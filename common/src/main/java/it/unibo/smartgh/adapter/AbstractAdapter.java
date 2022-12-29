package it.unibo.smartgh.adapter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public abstract class AbstractAdapter<API>  {

	private final Vertx vertx;
	
	private final API model;
	
	protected AbstractAdapter(API model, Vertx vertx) {
		this.model = model;
		this.vertx = vertx;
	}
	
	protected Vertx getVertx() {
		return vertx;
	}

	protected API getModel() {
		return model;
	}
	
	abstract protected void setupAdapter(Promise<Void> startPromise);
	
}
