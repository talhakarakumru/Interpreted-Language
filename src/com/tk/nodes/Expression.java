package com.tk.nodes;

import com.tk.Interpreter;

public class Expression extends Node
{
    private Node left;
    private Node right;
    private char operator;

    private double value;

    public Expression(Node superNode, Node left, Node right, char operator)
    {
        super(superNode);

        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression(Node superNode, Node left)
    {
        this(superNode, left, null, '\0');
    }

    public Expression(double value)
    {
        super(null);

        this.value = value;
    }

    @Override
    public void run()
    {
    }

    private double getVariableVal(Variable variable) throws Exception
    {
        double val = 0.0f;

        // Check if the variable is valid.
        Variable checkVar = Interpreter.findVariable(superNode, variable.getId());

        if(checkVar != null)
        {
            // Check if the variable's value is double type.
            if(checkVar.getValue() instanceof Double)
                return (double) checkVar.getValue();

            else throw new Exception("You must only use numbers when you use expressions.");
        }

        throw new Exception("Uninitialized variable has been used.");
    }

    public double getValue() throws Exception
    {
        if(right == null)
        {
            if (left == null)
                return value;

            else
            {
                if (left instanceof Expression)
                    return ((Expression) left).getValue();

                else if (left instanceof Variable)
                    return getVariableVal((Variable) left);

            }
        }

        double leftVal, rightVal;
        leftVal = rightVal = 0;

        if(left instanceof Expression)
            leftVal = ((Expression) left).getValue();

        else if(left instanceof Variable)
            leftVal = getVariableVal((Variable) left);

        if(right instanceof Expression)
            rightVal = ((Expression) right).getValue();

        else if(right instanceof Variable)
            rightVal = getVariableVal((Variable) right);

        switch(operator)
        {
            case '+':
                return leftVal + rightVal;

            case '-':
                return leftVal - rightVal;

            case '*':
                return leftVal * rightVal;

            case '/':
                if(rightVal != 0)
                    return leftVal / rightVal;
                else break;
        }

        return 0.0f;
    }
}
