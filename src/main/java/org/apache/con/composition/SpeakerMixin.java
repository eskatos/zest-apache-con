package org.apache.con.composition;

public class SpeakerMixin
    implements Speaker
{
    @Override
    public String sayHelloTo( String name )
    {
        return "Hello " + name;
    }

    @Override
    public String sayGoodbyeTo( String name )
    {
        return "Goodbye " + name;
    }
}
