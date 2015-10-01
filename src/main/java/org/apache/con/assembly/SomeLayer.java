package org.apache.con.assembly;

import org.apache.con.composition.Speaker;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.layered.LayerAssembler;

public class SomeLayer
    implements LayerAssembler
{
    @Override
    public LayerAssembly assemble( LayerAssembly layer )
        throws AssemblyException
    {
        ModuleAssembly some = layer.module( "Some Module" );
        ModuleAssembly other = layer.module( "Other Module" );
        some.transients( Speaker.class );
        return layer;
    }
}
