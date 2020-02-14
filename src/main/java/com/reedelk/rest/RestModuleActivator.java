package com.reedelk.rest;

import com.reedelk.rest.client.HttpClientFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = RestModuleActivator.class, scope = SINGLETON, immediate = true)
public class RestModuleActivator {

    // TODO: Should we stop the Listener Servers if any started!?
    @Reference
    private HttpClientFactory factory;

    @Deactivate
    public void deactivate() {
        factory.shutdown();
    }
}
