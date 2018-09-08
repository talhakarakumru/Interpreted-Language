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
    public void run() throws Exception
    {
        Variable var = (Variable) left;

        if(right instanceof Expression)
            var.setValue(((Expression) right).getValue());

        else if(right instanceof Text)
            var.setValue(((Text) right).getValue());

        // Check if its super nodes or global scope has a variable that has the same name.
        Variable checkVar = Interpreter.findVariable(superNode, var.getId());

        if(checkVar == null)
        {
            if(superNode != null)
            {
                if(superNode instanceof Function)
                {
                    Function f = (Function) superNode;
                    f.addVar(var);
                }
            }

            else Interpreter.globalVarScope.add(var);
        }

        // Re-assign it.
        else checkVar.setValue(var.getValue());
    }
}
