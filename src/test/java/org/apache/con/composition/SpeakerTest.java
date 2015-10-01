package org.apache.con.composition;

import org.junit.Test;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SpeakerTest
    extends AbstractQi4jTest
{
    @Override
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.transients( Speaker.class );
    }

    @Test
    public void givenSpeakerWhenSayThenExpectCorrectResult()
    {
        Speaker speaker = module.newTransient( Speaker.class );
        assertThat( speaker.sayHelloTo( "John" ), equalTo( "Hello John!" ) );
        assertThat( speaker.sayGoodbyeTo( "John" ), equalTo( "Goodbye John!" ) );
    }
}
