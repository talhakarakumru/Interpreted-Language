package com.tk.nodes;

import com.tk.Interpreter;

public class Echo extends Node
{
    private Node content;
    private boolean hasNextLine;

    public Echo(Node superNode, Node content, boolean hasNextLine)
    {
        super(superNode);

        this.content = content;
        this.hasNextLine = hasNextLine;
    }

    @Override
    public void run() throws Exception
    {
        if(content instanceof Text)
        {
            Text text = (Text) content;

            if(!hasNextLine)
                System.out.print(text.getValue());

            else System.out.println(text.getValue());
        }

        else if(content instanceof MyBoolean)
        {
            MyBoolean bool = (MyBoolean) content;

            if(!hasNextLine)
                System.out.print(bool.getValue());

            else System.out.println(bool.getValue());
        }

        else if(content instanceof Variable)
        {
            Variable variable = (Variable) content;

            Variable checkVar = Interpreter.findVariable(superNode, variable.getId());

            if(checkVar != null)
            {
                if(!hasNextLine)
                    System.out.print(checkVar.getValue());

                else System.out.println(checkVar.getValue());
            }

            else throw new Exception("Uninitialized variable has been used in echo.");
        }

        else if(content instanceof Expression)
        {
            if(!hasNextLine)
                System.out.print(((Expression) content).getValue());

            else System.out.println(((Expression) content).getValue());
        }
    }

    //region Getters and Setters
    //endregion
}
