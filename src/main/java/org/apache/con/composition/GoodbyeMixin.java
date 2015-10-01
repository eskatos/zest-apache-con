package org.apache.con.composition;

public abstract class GoodbyeMixin
    implements Speaker
{
    @Override
    public String sayGoodbyeTo( String name )
    {
        return "Goodbye " + name;
    }
}
