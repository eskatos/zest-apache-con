package org.apache.con.todos.domain;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceActivation;
import org.qi4j.api.structure.Module;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.usecase.UsecaseBuilder;

@Mixins( InitialData.Mixin.class )
public interface InitialData
    extends ServiceActivation
{
    class Mixin
        implements ServiceActivation
    {
        private static final String TODO_1_ID = "join-zest";

        @Structure
        private Module module;

        @Override
        public void activateService()
            throws Exception
        {
            try( UnitOfWork uow = module.newUnitOfWork( UsecaseBuilder.newUsecase( "Check for Initial Data" ) ) )
            {
                uow.get( Todo.class, TODO_1_ID );
            }
            catch( NoSuchEntityException ex )
            {
                // Not found, need to insert initial data
                try( UnitOfWork uow = module.newUnitOfWork( UsecaseBuilder.newUsecase( "Insert Initial Data" ) ) )
                {
                    EntityBuilder<Todo> builder = uow.newEntityBuilder( Todo.class, "apache-con" );
                    builder.instance().title().set( "Attend ApacheCon Core EU 2015" );
                    builder.instance().completed().set( true );
                    builder.newInstance();
                    builder = uow.newEntityBuilder( Todo.class, TODO_1_ID );
                    builder.instance().title().set( "Join the Apache Zest™ Community" );
                    builder.newInstance();
                    uow.complete();
                }
            }
        }

        @Override
        public void passivateService()
            throws Exception
        {
        }
    }
}
