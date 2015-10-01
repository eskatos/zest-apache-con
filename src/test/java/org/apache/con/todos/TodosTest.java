package org.apache.con.todos;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.bootstrap.AssemblyException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

public class TodosTest
{
    private static HttpServer server;

    @BeforeClass
    public static void startTodos()
        throws AssemblyException, ActivationException
    {
        server = TodosMain.startServer();
    }

    @AfterClass
    public static void stopTodos()
    {
        if( server != null )
        {
            server.shutdownNow();
        }
    }

    @Test
    public void list()
    {
        when().get( "/todos" )
            .then().statusCode( 200 )
            .body( containsString( "join-zest" ) )
            .body( containsString( "apache-con" ) )
            .log().all();
        when().get( "/todos?c=true" )
            .then().statusCode( 200 )
            .body( not( containsString( "join-zest" ) ) )
            .body( containsString( "apache-con" ) )
            .log().all();
        when().get( "/todos?c=false" )
            .then().statusCode( 200 )
            .body( containsString( "join-zest" ) )
            .body( not( containsString( "apache-con" ) ) )
            .log().all();
        when().get( "/todos?q=Zest" )
            .then().statusCode( 200 )
            .body( containsString( "join-zest" ) )
            .body( not( containsString( "apache-con" ) ) )
            .log().all();
        when().get( "/todos?q=Zest&c=true" )
            .then().statusCode( 200 )
            .body( not( containsString( "join-zest" ) ) )
            .body( not( containsString( "apache-con" ) ) )
            .log().all();
    }

    @Test
    public void get()
    {
        when().get( "/todos/123" )
            .then().statusCode( 404 )
            .log().all();

        when().get( "/todos/join-zest" )
            .then().statusCode( 200 )
            .log().all();
    }

    @Test
    public void crud()
    {
        // Create
        given().contentType( "application/json" )
            .body( "{\"identity\":\"foo\", \"title\": \"bar\"}" )
            .when().post( "/todos" )
            .then().statusCode( 201 )
            .and().header( "Location", "http://localhost:8080/todos/foo" )
            .log().all();
        // Get
        when().get( "/todos/foo" )
            .then().statusCode( 200 )
            .body( containsString( "bar" ) )
            .log().all();
        // Update
        given().contentType( "application/json" )
            .body( "{\"identity\":\"foo\", \"title\": \"bazar\"}" )
            .when().put( "/todos/foo" )
            .then().statusCode( 200 )
            .body( not( containsString( "bar" ) ) )
            .body( containsString( "bazar" ) )
            .log().all();
        // Get
        when().get( "/todos/foo" )
            .then().statusCode( 200 )
            .body( containsString( "bazar" ) )
            .log().all();
        // List
        when().get( "/todos" )
            .then().statusCode( 200 )
            .body( containsString( "foo" ) )
            .log().all();
        // Delete
        when().delete( "/todos/foo" )
            .then().statusCode( 204 )
            .log().all();
        // List
        when().get( "/todos" )
            .then().statusCode( 200 )
            .body( not( containsString( "foo" ) ) )
            .log().all();
    }
}
