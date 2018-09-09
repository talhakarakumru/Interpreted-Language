package com.tk.nodes;

import java.util.LinkedList;

public class Function extends Node
{
    private String id;
    private LinkedList<Variable> varScope;

    public Function(Node superNode, String id)
    {
        super(superNode);

        this.id = id;
        this.varScope = new LinkedList<>();
    }

    @Override
    public void run() throws Exception
    {
        for(Node subNode : subNodes)
            subNode.run();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Function)
        {
            Function f = (Function) obj;

            if(id.equals(f.getId()))
                return true;
        }

        return false;
    }

    public void addVar(Variable var)
    {
        varScope.add(var);
    }

    //region Getters and Setters
    public String getId()
    {
        return id;
    }

    public LinkedList<Variable> getVarScope()
    {
        return varScope;
    }
    //endregion
}
