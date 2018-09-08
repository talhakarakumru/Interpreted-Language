package com.tk.nodes;

import com.tk.Interpreter;

public class Echo extends Node
{
    private Node content;

    public Echo(Node superNode, Node content)
    {
        super(Type.ECHO, superNode);

        this.content = content;
    }

    @Override
    public void run() throws Exception
    {
        if(content instanceof Text)
        {
            Text text = (Text) content;
            System.out.println(text.getValue());
        }

        else if(content instanceof Variable)
        {
            Variable variable = (Variable) content;

            Variable checkVar = Interpreter.findVariable(superNode, variable.getId());

            if(checkVar != null)
                 System.out.println(checkVar.getValue());

            else throw new Exception("Uninitialized variable has been used in echo.");
        }

        else if(content instanceof Expression)
            System.out.println(((Expression) content).getValue());
    }

    //region Getters and Setters
    //endregion
}
