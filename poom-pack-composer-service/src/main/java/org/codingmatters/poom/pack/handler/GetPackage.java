package org.codingmatters.poom.pack.handler;

import org.codingmatters.poom.pack.api.PackagesGetRequest;
import org.codingmatters.poom.pack.api.PackagesGetResponse;
import org.codingmatters.poom.pack.api.types.JsonPackage;
import org.codingmatters.value.objects.values.ObjectValue;

import java.io.File;
import java.io.FileFilter;
import java.util.function.Function;

public class GetPackage implements Function<PackagesGetRequest, PackagesGetResponse> {

    private final File repository;
    private final String serviceUrl;

    public GetPackage( String repositoryPath, String serviceUrl ) {
        this.repository = new File( repositoryPath );
        this.serviceUrl = serviceUrl;
    }

    @Override
    public PackagesGetResponse apply( PackagesGetRequest packagesGetRequest ) {
        FileFilter directories = File::isDirectory;
        File[] vendors = repository.listFiles( directories );
        ObjectValue.Builder packages = ObjectValue.builder();
        if( vendors != null ) {
            for( File vendor : vendors ) {
                File[] packageNames = vendor.listFiles( directories );
                if( packageNames != null ) {
                    for( File packageName : packageNames ) {
                        ObjectValue.Builder vendorPackage = ObjectValue.builder();
                        String packageFullName = vendor.getName() + "/" + packageName.getName();
                        File[] versions = packageName.listFiles( directories );
                        if( versions != null ) {
                            ObjectValue.Builder composer = ObjectValue.builder();
                            for( File version : versions ) {
                                String[] composerFile = version.list( ( dir, name )->name.equals( "composer.json" ) );
                                String versionName = version.getName();
                                if( composerFile != null && composerFile.length == 1 ) {
                                    composer.property( "name", name->name.stringValue( packageFullName ) )
                                            .property( "version", packVersion->packVersion.stringValue( versionName ) )
                                            .property( "dist", name->name.objectValue( ObjectValue.builder()
                                                    .property( "url", url->url.stringValue( serviceUrl + "/" + String.join( "/", packageFullName, versionName ) ) )
                                                    .property( "type", type->type.stringValue( "zip" ) )
                                                    .build() ) );

                                }
                                vendorPackage.property( versionName, v->v.objectValue( composer.build() ) );
                            }
                            packages.property( packageFullName, p->p.objectValue( vendorPackage.build() ) );
                        }
                    }
                }
            }
        }
        JsonPackage.Builder jsonPack = JsonPackage.builder();
        jsonPack.packages( packages.build() );
        return PackagesGetResponse.builder().status200( status->status.payload( jsonPack.build() ) ).build();

    }

}
