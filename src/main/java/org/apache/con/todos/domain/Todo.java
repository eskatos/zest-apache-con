package org.apache.con.todos.domain;

import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.property.Property;
import org.qi4j.library.constraints.annotation.MinLength;

public interface Todo
    extends Identity
{
    @MinLength( 2 )
    Property<String> title();

    @UseDefaults
    Property<Boolean> completed();
}
