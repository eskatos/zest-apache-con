package org.apache.con.composition;

public abstract class HelloMixin
    implements Speaker
{
    @Override
    public String sayHelloTo( String name )
    {
        return "Hello " + name;
    }
}
