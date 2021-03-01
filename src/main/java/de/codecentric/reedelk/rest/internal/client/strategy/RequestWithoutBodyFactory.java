package de.codecentric.reedelk.rest.internal.client.strategy;

import org.apache.http.client.methods.HttpRequestBase;

public interface RequestWithoutBodyFactory {
    HttpRequestBase create();
}
