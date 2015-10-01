package org.apache.con.assembly;

import org.qi4j.api.structure.Application;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.layered.LayeredApplicationAssembler;

public class MyApplicationAssembler
    extends LayeredApplicationAssembler
{
    public MyApplicationAssembler( Application.Mode mode )
        throws AssemblyException
    {
        super( "Apache Con", "1.0", mode );
    }

    @Override
    protected void assembleLayers( ApplicationAssembly assembly )
        throws AssemblyException
    {
        LayerAssembly someLayer = createLayer( SomeLayer.class );
    }
}
