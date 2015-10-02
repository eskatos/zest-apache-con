package org.apache.con.composition;

import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.structure.Module;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.SingletonAssembler;

public class CompositionMain
{
    public static void main( String[] args )
        throws AssemblyException, ActivationException
    {
        SingletonAssembler assembler = new SingletonAssembler()
        {
            @Override
            public void assemble( ModuleAssembly module )
                throws AssemblyException
            {
                module.transients( Speaker.class )
                    .withMixins( HelloMixin.class, GoodbyeMixin.class )
                    .withConcerns( ExclamationConcern.class )
                    .withSideEffects( MailNotifySideEffect.class );
            }
        };
        Module module = assembler.module();

        Speaker speaker = module.newTransient( Speaker.class );

        System.out.println( speaker.sayHelloTo( "apachees" ) );
        System.out.println( speaker.sayGoodbyeTo( "apachees" ) );
    }
}
