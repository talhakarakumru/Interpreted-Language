package com.tk.nodes;

public class MyBoolean extends Node
{
    boolean value;

    public MyBoolean(Node superNode, boolean value)
    {
        super(superNode);

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
