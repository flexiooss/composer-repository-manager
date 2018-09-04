package org.codingmatters.poom.pack;

import org.codingmatters.poom.pack.api.RepositoryPostRequest;
import org.codingmatters.poom.pack.handler.SavePackage;
import org.codingmatters.rest.api.types.File;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SavePackageTest {
    private final String API_KEY = "9";

    private TemporaryFolder temp;
    private SavePackage savePackage;

    @Before
    public void setUp() throws Exception {
        temp = new TemporaryFolder();
        temp.create();
        savePackage = new SavePackage( temp.getRoot().getPath(), API_KEY );
    }

    @Test
    public void testDeployWithBadApiKey_thenReturn403() throws IOException {
        byte[] content = new byte[]{ 0x01 };
        savePackage.apply(
                RepositoryPostRequest.builder()
                        .xApiKey( "BAD_KEY" )
                        .xVendor( "vendor" )
                        .xArtifactId( "artifact" )
                        .xVersion( "1.0" )
                        .payload( file->file.content( content ).contentType( "text/plain" ) )
                        .build()
        ).opt().status403().orElseThrow( ()->new AssertionError( "Should not upload something with bad api key" ) );
    }

    @Test
    public void testDeployNewPackage_thenDirAreCreatedAndZipFileSaved() throws IOException {
        Path zipArchive = Paths.get( Thread.currentThread().getContextClassLoader().getResource( "flexio-tabular-php-client-1.0.0-SNAPSHOT.zip" ).getPath() );
        byte[] content = Files.readAllBytes( zipArchive );

        savePackage.apply(
                RepositoryPostRequest.builder()
                        .xVendor( "flexio-services" )
                        .xArtifactId( "flexio-tabular-php-client" )
                        .xVersion( "1.0.0-SNAPSHOT" )
                        .xApiKey( API_KEY )
                        .payload( File.builder()
                                .content( content )
                                .contentType( "application/zip" )
                                .build() )
                        .build()
        ).opt().status201().orElseThrow( ()->new AssertionError( "Cannot save the artifact" ) );

        java.io.File[] vendorDir = temp.getRoot().listFiles( ( dir, name )->name.equals( "flexio-services" ) );
        assertThat( vendorDir, notNullValue() );
        assertThat( vendorDir.length, is( 1 ) );
        assertThat( vendorDir[0].isDirectory(), is( true ) );

        java.io.File[] packageDir = vendorDir[0].listFiles( ( dir, name )->name.equals( "flexio-tabular-php-client" ) );
        assertThat( packageDir, notNullValue() );
        assertThat( packageDir.length, is( 1 ) );
        assertThat( packageDir[0].isDirectory(), is( true ) );

        java.io.File[] versionDir = packageDir[0].listFiles( ( dir, name )->name.equals( "1.0.0-dev" ) );
        assertThat( versionDir, notNullValue() );
        assertThat( versionDir.length, is( 1 ) );
        assertThat( vendorDir[0].isDirectory(), is( true ) );

        java.io.File[] zipFile = versionDir[0].listFiles( ( dir, name )->"flexio-tabular-php-client-1.0.0-dev.zip".equals( name ) );
        assertThat( zipFile, notNullValue() );
        assertThat( Files.readAllBytes( zipFile[0].toPath() ), is( content ) );
    }
}
