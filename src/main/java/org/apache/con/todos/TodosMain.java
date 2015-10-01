package org.apache.con.todos;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.constraint.ConstraintViolationException;
import org.qi4j.api.structure.Application;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.library.jersey.Qi4jFeature;

import static org.apache.con.todos.TodosApplicationAssembler.LAYER_CONNECTIVITY;
import static org.apache.con.todos.TodosApplicationAssembler.MODULE_REST;

public class TodosMain
{
    static final String BASE_URI = "http://localhost:8080/";

    static HttpServer startServer()
        throws AssemblyException, ActivationException
    {
        // Zest Application
        Application application = new Energy4Java().newApplication( new TodosApplicationAssembler() );

        // Jersey Application
        Feature qi4jFeature = new Qi4jFeature( application, LAYER_CONNECTIVITY, MODULE_REST );
        final ResourceConfig rc = new ResourceConfig()
            .packages( "org.apache.con.todos.resources" )
            .register( qi4jFeature )
            .register( new ErrorHandler() );

        // Grizzly Server
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), rc );
    }

    public static void main( String[] args )
        throws AssemblyException, ActivationException, IOException
    {
        final HttpServer server = startServer();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                server.shutdownNow();
            }
        } ) );
        System.out.println( String.format( "Zest Todos Application started and available at %s", BASE_URI ) );
    }
}

class ErrorHandler
    implements ExceptionMapper<Throwable>
{
    @Override
    public Response toResponse( Throwable exception )
    {
        if( exception instanceof WebApplicationException )
        {
            return ( (WebApplicationException) exception ).getResponse();
        }
        if( exception instanceof NoSuchEntityException )
        {
            return Response.status( Status.NOT_FOUND )
                .entity( exception.getMessage() )
                .build();
        }
        if( exception instanceof ConstraintViolationException )
        {
            return Response.status( Status.PRECONDITION_FAILED )
                .entity( exception.getMessage() )
                .build();
        }
        throw new WebApplicationException( exception );
    }
}
