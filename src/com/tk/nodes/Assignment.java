package com.tk.nodes;

import com.tk.Interpreter;

public class Assignment extends Node
{
    private Node left;
    private Node right;

    public Assignment(Node superNode, Node left, Node right)
    {
        super(Type.ASSIGNMENT, superNode);

        this.left = left;
        this.right = right;
    }

    @Override
    public void run()
    {
        Variable var = (Variable) left;

        if(right instanceof Expression)
            var.setValue(((Expression) right).getValue());

        else if(right instanceof Text)
            var.setValue(((Text) right).getValue());

        if(superNode != null)
        {
            if(superNode instanceof Function)
                ((Function) superNode).addVar(var);
        }

        else Interpreter.globalVarScope.add(var);
    }
}
