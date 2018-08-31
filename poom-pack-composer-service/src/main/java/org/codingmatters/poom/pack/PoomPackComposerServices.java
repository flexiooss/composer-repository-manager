package org.codingmatters.poom.pack;

import io.flexio.services.metrics.HealthCheckProcessor;
import io.flexio.services.support.api.ApiService;
import io.flexio.services.support.args.Arguments;
import io.flexio.services.support.args.ArgumentsException;
import org.codingmatters.value.objects.values.ObjectValue;

import static org.codingmatters.poom.services.support.Env.SERVICE_HOST;
import static org.codingmatters.poom.services.support.Env.SERVICE_PORT;

public class PoomPackComposerServices {
    
    public static void main(String[] args){
        try{
            ApiService service = fromArguments(Arguments.parse(args));
            service.go();
        } catch (ArgumentsException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public static ApiService fromArguments( Arguments arguments ) throws ArgumentsException {
        return new ApiService(
                arguments.mandatoryString( SERVICE_HOST ),
                arguments.mandatoryInteger( SERVICE_PORT ),
                ()->{},
                HealthCheckProcessor.defaultHealthProcessor( ObjectValue.builder()::build )
        );
    }
    
}

