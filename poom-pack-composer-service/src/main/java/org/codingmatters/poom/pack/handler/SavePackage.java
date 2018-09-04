package org.codingmatters.poom.pack.handler;

import org.codingmatters.poom.pack.api.RepositoryPostRequest;
import org.codingmatters.poom.pack.api.RepositoryPostResponse;
import org.codingmatters.poom.pack.api.types.Error;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class SavePackage implements Function<RepositoryPostRequest, RepositoryPostResponse> {

    private final CategorizedLogger log = CategorizedLogger.getLogger( SavePackage.class );
    private final File repository;
    private final String apiKey;

    public SavePackage( String repositoryPath, String apiKey ) {
        this.repository = new File( repositoryPath );
        this.apiKey = apiKey;
    }

    @Override
    public RepositoryPostResponse apply( RepositoryPostRequest repositoryPostRequest ) {
        if( !apiKey.equals( repositoryPostRequest.xApiKey() ) ) {
            return RepositoryPostResponse.builder()
                    .status403( status->status.build() )
                    .build();
        }
        String composerCompliantVersion = formatVersion( repositoryPostRequest.xVersion() );

        String artifactDirectory = String.join( File.separator,
                repository.getPath(),
                repositoryPostRequest.xVendor(),
                repositoryPostRequest.xArtifactId(),
                composerCompliantVersion
        );

        log.info( String.format( "Creating artifact: %s-%s in %s", repositoryPostRequest.xArtifactId(), composerCompliantVersion, artifactDirectory ) );
        File artifactDir = new File( artifactDirectory );
        if( artifactDir.exists() || artifactDir.mkdirs() ) {
            String targetFile = artifactDirectory + "/" + repositoryPostRequest.xArtifactId() + "-" + composerCompliantVersion + ".zip";
            byte[] buffer = new byte[1024];
            try( FileOutputStream outputStream = new FileOutputStream( new File( targetFile ) ) ) {
                outputStream.write( repositoryPostRequest.payload().content() );
                /*
                try( InputStream inputStream = repositoryPostRequest.payload().inputStream() ) {
                    int read;
                    while( (read = inputStream.read( buffer )) != -1 ) {
                        log.info( "Write " + read + " bytes to " + targetFile );
                        outputStream.write( buffer );
                    }
                }
                */
                outputStream.flush();
                return RepositoryPostResponse.builder()
                        .status201( status->status.build() ).build();
            } catch( IOException e ) {
                return RepositoryPostResponse.builder()
                        .status500( status->status.payload( err->err
                                .code( Error.Code.UNEXPECTED_ERROR )
                                .description( "Cannot create dir to store artifact" ) ) )
                        .build();
            }
        } else {
            return RepositoryPostResponse.builder()
                    .status500( status->status.payload( err->err
                            .code( Error.Code.UNEXPECTED_ERROR )
                            .description( "Cannot create dir to store artifact" ) ) )
                    .build();
        }
    }

    private String formatVersion( String version ) {
        return version.replace( "SNAPSHOT", "dev" );
    }
}
