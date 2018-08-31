package org.codingmatters.poom.pack.handler;

import org.codingmatters.poom.pack.api.RepositoryPostRequest;
import org.codingmatters.poom.pack.api.RepositoryPostResponse;

import java.io.File;
import java.util.function.Function;

public class SavePackage implements Function<RepositoryPostRequest, RepositoryPostResponse> {

    private final File repository;

    public SavePackage( String repositoryPath ) {
        this.repository = new File( repositoryPath );
    }

    @Override
    public RepositoryPostResponse apply( RepositoryPostRequest repositoryPostRequest ) {

        return null;
    }
}
