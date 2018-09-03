package com.tk.nodes;

import com.tk.Interpreter;

import java.util.Iterator;

public class Call extends Node
{
    private String id;

    public Call(Node superNode, String id)
    {
        super(Type.CALL, superNode);

        this.id = id;
    }

    @Override
    public void run()
    {
        Function f;

        for(Iterator<Function> iter = Interpreter.globalFunctionScope.iterator(); iter.hasNext();)
        {
            f = iter.next();

            if(f.getId().equals(id))
            {
                f.run();
                return;
            }
        }
    }

    //region Getters and Setters
    public String getId()
    {
        return id;
    }
    //endregion;
}
