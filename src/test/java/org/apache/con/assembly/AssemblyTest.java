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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AssemblyTest
{
    private static MyApplicationAssembler assembler;

    @BeforeClass
    public static void setApplicationUp()
        throws AssemblyException, ActivationException
    {
        assembler = new MyApplicationAssembler( Application.Mode.test );
        assembler.start();
    }

    @AfterClass
    public static void tearApplicationDown()
        throws PassivationException
    {
        if( assembler != null )
        {
            assembler.stop();
        }
    }

    @Test
    public void test()
    {
        Module other = assembler.application().findModule( "Some Layer", "Other Module" );
        Speaker speaker = other.newTransient( Speaker.class );
        assertThat( speaker.sayHelloTo( "John" ), equalTo( "Hello John!" ) );
        assertThat( speaker.sayGoodbyeTo( "John" ), equalTo( "Goodbye John!" ) );
    }
}
