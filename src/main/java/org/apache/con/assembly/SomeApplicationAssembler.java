package org.apache.con.assembly;

import org.apache.con.composition.Speaker;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;

public class SomeApplicationAssembler
    implements ApplicationAssembler
{
    @Override
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
        throws AssemblyException
    {
        ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
        assembly.setName( "Some Application" );
        assembly.setVersion( "1.0" );

        LayerAssembly someLayer = assembly.layer( "Some Layer" );
        ModuleAssembly someModule = someLayer.module( "Some Module" );
        someModule.transients( Speaker.class );

        ModuleAssembly otherModule = someLayer.module( "Other Module" );

        return assembly;
    }
}
