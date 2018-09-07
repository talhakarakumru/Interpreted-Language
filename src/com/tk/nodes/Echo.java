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

            System.out.println(variable.getId());

            // TODO: Fix this to make sure it gets one more step to another super node.
            // Check if it has a super node.
            if(superNode != null)
            {
                // Check it has the value
                if(superNode instanceof Function)
                {
                    Function supFunc = (Function) superNode;

                    for(Variable v : supFunc.getVarScope())
                    {
                        if(variable.equals(v))
                        {
                            System.out.println(v.getValue());
                            return;
                        }
                    }
                }

                // Check the global scope.
                for(Variable v : Interpreter.globalVarScope)
                {
                    if(variable.equals(v))
                    {
                        System.out.println(v.getValue());
                        return;
                    }
                }

                throw new Exception("Uninitialized variable was used.");
            }
        }

        else if(content instanceof Expression)
            System.out.println(((Expression) content).getValue());
    }

    //region Getters and Setters
    //endregion
}
