package org.codingmatters.poom.pack;

import com.fasterxml.jackson.core.JsonFactory;
import org.codingmatters.poom.pack.api.PoomPackComposerHandlers;
import org.codingmatters.poom.pack.handler.GetPackage;
import org.codingmatters.poom.pack.handler.SavePackage;
import org.codingmatters.poom.pack.service.PoomPackComposerProcessor;
import org.codingmatters.rest.api.Processor;


public class PoomPackComposerApi {

    private final String name = "poom-pack-composer";
    private PoomPackComposerHandlers handlers;
    private PoomPackComposerProcessor processor;

    public PoomPackComposerApi( String repositoryPath, String serviceUrl ) {
        this.handlers = new PoomPackComposerHandlers.Builder()
                .packagesGetHandler( new GetPackage( repositoryPath, serviceUrl ) )
                .repositoryPostHandler( new SavePackage( repositoryPath ) )
                .build();
        this.processor = new PoomPackComposerProcessor( this.path(), new JsonFactory(), handlers );
    }

    public Processor processor() {
        return this.processor;
    }


    public String path() {
        return "/" + this.name;
    }

}

