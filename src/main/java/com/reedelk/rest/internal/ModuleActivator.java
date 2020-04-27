package com.reedelk.rest.internal;

import com.reedelk.rest.internal.client.HttpClientFactory;
import com.reedelk.rest.internal.script.GlobalFunctions;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = ModuleActivator.class, scope = SINGLETON, immediate = true)
public class ModuleActivator {

    // TODO: Should we stop the Listener Servers if any started!?
    @Reference
    private HttpClientFactory factory;
    @Reference
    private ScriptEngineService scriptEngine;

    @Activate
    public void start(BundleContext context) {
        long moduleId = context.getBundle().getBundleId();
        GlobalFunctions globalFunctions = new GlobalFunctions(moduleId);
        scriptEngine.register(globalFunctions);
    }

    @Deactivate
    public void deactivate() {
        factory.shutdown();
    }
}
