package com.tk.nodes;

public class MyBoolean extends Node
{
    boolean value;

    public MyBoolean(boolean value)
    {
        super(Type.BOOLEAN, null);

        this.value = value;
    }

    @Override
    public void run() throws Exception
    {

    }

    //region Getters and Setters
    public boolean getValue()
    {
        return value;
    }
    //endregion
}
