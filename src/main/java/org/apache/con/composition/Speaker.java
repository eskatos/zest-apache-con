package org.apache.con.composition;

import org.qi4j.library.constraints.annotation.MinLength;

public interface Speaker
{
    String sayHelloTo( @MinLength( 2 ) String name );

    String sayGoodbyeTo( String name );
}
