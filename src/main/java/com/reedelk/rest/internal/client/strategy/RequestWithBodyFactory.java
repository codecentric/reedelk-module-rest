package com.reedelk.rest.internal.client.strategy;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public interface RequestWithBodyFactory {

    HttpEntityEnclosingRequestBase create();

}
