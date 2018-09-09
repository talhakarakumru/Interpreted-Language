package com.tk.nodes;

public class Variable <T> extends Node
{
    private String id;
    private T value;

    public Variable(Node superNode, String id)
    {
        super(superNode);
        this.id = id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Variable)
        {
            Variable other = (Variable) obj;

            if(id.equals(other.getId()))
                return true;
        }

        return false;
    }

    //region Getters and Setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public void run()
    {

    }
    //endregion
}
