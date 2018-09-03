package com.tk.nodes;

import com.tk.Variable;

import java.util.LinkedList;

public class Function extends Node
{
    private String id;
    private LinkedList<Variable> varScope;

    public Function(Node superNode, String id)
    {
        super(Type.FUNCTION, superNode);

        this.id = id;
        this.varScope = new LinkedList<>();
    }

    @Override
    public void run()
    {
        for(Node subNode : subNodes)
            subNode.run();
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
