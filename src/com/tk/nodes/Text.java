package com.tk.nodes;

public class Text extends Node
{
    private String value;

    public Text(String value)
    {
        super(Type.TEXT, null);

        this.value = value;
    }

    @Override
    public void run()
    {

    }

    //region Getters and Setters
    public String getValue()
    {
        return value;
    }
    //endregion
}
