package com.tk;

public class Variable <T>
{
    private String id;
    private T value;
    private Type type;

    public Variable(String id, T value, Type type)
    {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Variable)
        {
            Variable other = (Variable) obj;

            if(value.equals(other.getValue()))
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

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
    //endregion

    public enum Type
    {
        STRING,
        NUMBER
    }
}
