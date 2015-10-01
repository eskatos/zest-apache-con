package org.apache.con.composition;

import org.qi4j.api.concern.ConcernOf;

public class ExclamationConcern
    extends ConcernOf<Speaker>
    implements Speaker
{
    @Override
    public String sayHelloTo( String name )
    {
        return next.sayHelloTo( name ) + "!";
    }

    @Override
    public String sayGoodbyeTo( String name )
    {
        return next.sayGoodbyeTo( name ) + "!";
    }
}
