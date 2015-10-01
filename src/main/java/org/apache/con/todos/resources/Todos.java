package org.apache.con.todos.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.con.todos.domain.Todo;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.structure.Module;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;

import static org.qi4j.api.query.QueryExpressions.eq;
import static org.qi4j.api.query.QueryExpressions.matches;
import static org.qi4j.api.query.QueryExpressions.templateFor;
import static org.qi4j.api.usecase.UsecaseBuilder.newUsecase;

@Path( "todos" )
public class Todos
{
    @Structure
    private Module module;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<Todo> list( @QueryParam( "q" ) String q, @QueryParam( "c" ) Boolean c )
    {
        try( UnitOfWork uow = module.newUnitOfWork( newUsecase( "List TODOs" ) ) )
        {
            QueryBuilder<Todo> queryBuilder = module.newQueryBuilder( Todo.class );
            Todo queryTemplate = templateFor( Todo.class );
            if( q != null && q.length() > 0 )
            {
                queryBuilder = queryBuilder.where( matches( queryTemplate.title(), String.format( ".*%s.*", q ) ) );
            }
            if( c != null )
            {
                queryBuilder = queryBuilder.where( eq( queryTemplate.completed(), c ) );
            }
            Query<Todo> query = uow.newQuery( queryBuilder );
            List<Todo> result = new ArrayList<>();
            for( Todo todo : query )
            {
                result.add( uow.toValue( Todo.class, todo ) );
            }
            return result;
        }
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response create( Todo todo, @Context UriInfo uriInfo )
        throws UnitOfWorkCompletionException
    {
        try( UnitOfWork uow = module.newUnitOfWork( newUsecase( "Create TODO" ) ) )
        {
            uow.toEntity( Todo.class, todo );
            uow.complete();
            URI uri = uriInfo.getAbsolutePathBuilder().path( todo.identity().get() ).build();
            return Response.created( uri ).build();
        }
    }

    @GET
    @Path( "{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Todo get( @PathParam( "id" ) String identity )
    {
        try( UnitOfWork uow = module.newUnitOfWork( newUsecase( "Get TODO" ) ) )
        {
            Todo todo = uow.get( Todo.class, identity );
            return uow.toValue( Todo.class, todo );
        }
    }

    @PUT
    @Path( "{id}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Todo update( Todo todo )
        throws UnitOfWorkCompletionException
    {
        try( UnitOfWork uow = module.newUnitOfWork( newUsecase( "Update TODO" ) ) )
        {
            Todo updated = uow.toEntity( Todo.class, todo );
            Todo value = uow.toValue( Todo.class, updated );
            uow.complete();
            return value;
        }
    }

    @DELETE
    @Path( "{id}" )
    public void delete( @PathParam( "id" ) String identity )
        throws UnitOfWorkCompletionException
    {
        try( UnitOfWork uow = module.newUnitOfWork( newUsecase( "Delete TODO" ) ) )
        {
            Todo todo = uow.get( Todo.class, identity );
            uow.remove( todo );
            uow.complete();
        }
    }
}
