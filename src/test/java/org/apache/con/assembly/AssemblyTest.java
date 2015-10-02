package org.apache.con.assembly;

import org.apache.con.composition.Speaker;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Module;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AssemblyTest
{
    private static Application application;

    @BeforeClass
    public static void setApplicationUp()
        throws AssemblyException, ActivationException
    {
        application = new Energy4Java().newApplication( new SomeApplicationAssembler() );
    }

    @AfterClass
    public static void tearApplicationDown()
        throws PassivationException
    {
        if( application != null )
        {
            application.passivate();
        }
    }

    @Test
    public void test()
    {
        Module other = application.findModule( "Some Layer", "Other Module" );
        Speaker speaker = other.newTransient( Speaker.class );
        assertThat( speaker.sayHelloTo( "John" ), equalTo( "Hello John!" ) );
        assertThat( speaker.sayGoodbyeTo( "John" ), equalTo( "Goodbye John!" ) );
    }
}
