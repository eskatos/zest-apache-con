package org.apache.con.composition;

import java.util.Date;

import org.qi4j.api.sideeffect.SideEffectOf;

public class MailNotifySideEffect
    extends SideEffectOf<Speaker>
    implements Speaker
{
    @Override
    public String sayHelloTo( String name )
    {
        String said = result.sayHelloTo( name );
        System.out.println( "  Sending email: " + new Date() + " said " + said );
        return said;
    }

    @Override
    public String sayGoodbyeTo( String name )
    {
        String said = result.sayGoodbyeTo( name );
        System.out.println( "  Sending email: " + new Date() + " said " + said );
        return said;
    }
}
