package org.codingmatters.poom.pack;

import io.flexio.services.support.api.Api;
import org.codingmatters.rest.api.Processor;
import com.fasterxml.jackson.core.JsonFactory;
import io.flexio.services.tabular.api.PoomPackComposerHandlers;
import io.flexio.services.tabular.service.PoomPackComposerProcessor;

public class PoomPackComposerApi implements Api {

    private final String name = "poom-pack-composer";
    private PoomPackComposerHandlers handlers;
    private PoomPackComposerProcessor processor;

    public PoomPackComposerApi(){
        this.handlers = new PoomPackComposerHandlers.Builder()
                            .build();

        this.processor = new PoomPackComposerProcessor(this.path(), new JsonFactory(), handlers);
    }

    @Override
    public Processor processor() {
        return this.processor;
    }

    @Override
    public String docResource() {
        return "poom-pack-composer.html";
    }

    @Override
    public String path() {
        return "/" + this.name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String version() {
        return "v2";
    }
}

