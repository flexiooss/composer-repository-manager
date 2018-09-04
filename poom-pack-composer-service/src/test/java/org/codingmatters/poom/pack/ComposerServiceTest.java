package org.codingmatters.poom.pack;

import io.flexio.services.support.api.test.TestEnvProvider;
import org.codingmatters.poom.services.support.Env;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class ComposerServiceTest {
/*
    @Rule
    public TestEnvProvider envProvider = new TestEnvProvider();

    @Test
    public void testService() throws IOException {
        int port = gatAvailablePort();
        envProvider.set( Env.SERVICE_PORT, "" + port );
        envProvider.set( Env.SERVICE_HOST, "localhost" );
        envProvider.set( Env.SERVICE_URL, "http://localhost:" + port + "/poom-pack-composer" );
        envProvider.set( "repository_path", "/tmp/zerepo" );
        PoomPackComposerServices.main( new String[]{} );
    }

    private int gatAvailablePort() throws IOException {
        ServerSocket s = new ServerSocket( 0 );
        return s.getLocalPort();
    }
*/
}
