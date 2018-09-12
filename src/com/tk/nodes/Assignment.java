package com.tk.nodes;

import com.tk.Interpreter;

public class Assignment extends Node
{
    private Node left;
    private Node right;

    public Assignment(Node superNode, Node left, Node right)
    {
        super(superNode);

        this.left = left;
        this.right = right;
    }

    @Override
    public void run() throws Exception
    {
        Variable var = (Variable) left;

        if(right instanceof Expression)
            var.setValue(((Expression) right).getValue());

        else if(right instanceof Text)
            var.setValue(((Text) right).getValue());

        else if(right instanceof MyBoolean)
            var.setValue(((MyBoolean) right).getValue());

        else if(right instanceof Variable)
        {
            Variable rightVar = (Variable) right;
            var.setValue(rightVar.getValue());
        }

        // Check if its super nodes or global scope has a variable that has the same name.
        Variable checkVar = Interpreter.findVariable(superNode, var.getId());

        if(checkVar != null)
        {
            // Chekc if it is instance of same class.
            if(var.getValue().getClass().equals(checkVar.getValue().getClass()))
                checkVar.setValue(var.getValue());

            else throw new Exception("You cannot assign with different type of value.");
        }

        else throw new Exception("Invalid assignment.");
    }
}
