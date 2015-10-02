package org.apache.con.composition;

import org.qi4j.api.concern.Concerns;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.sideeffect.SideEffects;
import org.qi4j.library.constraints.annotation.MinLength;

@Mixins({HelloMixin.class,GoodbyeMixin.class})
@Concerns(ExclamationConcern.class)
@SideEffects(MailNotifySideEffect.class)
public interface Speaker
{
    String sayHelloTo( @MinLength( 2 ) String name );

    String sayGoodbyeTo( String name );
}
