package org.apache.con.assembly;

import org.apache.con.composition.Speaker;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Module;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.envisage.Envisage;

public class AssemblyMain
{
    public static void main( String[] args )
        throws AssemblyException, ActivationException
    {
        Application application = new Energy4Java().newApplication( new SomeApplicationAssembler() );

        Module other = application.findModule( "Some Layer", "Other Module" );
        Speaker speaker = other.newTransient( Speaker.class );

        System.out.println( speaker.sayHelloTo( "apachees" ) );
        System.out.println( speaker.sayGoodbyeTo( "apachees" ) );

        new Envisage().run( application.descriptor() );
    }
}
