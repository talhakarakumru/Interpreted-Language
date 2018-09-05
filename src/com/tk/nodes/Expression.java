package com.tk.nodes;

public class Expression extends Node
{
    private Node left;
    private Node right;
    private char operator;

    private double value;

    public Expression(Node left, Node right, char operator)
    {
        super(Type.EXPRESSION, null);

        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression(Node left)
    {
        this(left, null, '\0');
    }

    public Expression(double value)
    {
        super(Type.EXPRESSION, null);

        this.value = value;
    }

    @Override
    public void run()
    {
    }

    public double getValue()
    {
        if(left == null)
            return value;

        Expression l, r;
        l = (Expression) left;
        r = (Expression) right;

        switch(operator)
        {
            case '+':
                return l.getValue() + r.getValue();

            case '-':
                return l.getValue() - r.getValue();

            case '*':
                return l.getValue() * r.getValue();

            case '/':
                if(r.getValue() > 0)
                    return l.getValue() / r.getValue();
                else break;
        }

        return 0.0f;
    }
}
