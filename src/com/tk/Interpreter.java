package com.tk;

public class Interpreter
{
    private Lexer lexer;

    public Interpreter(String code)
    {
        this.lexer = new Lexer(code);
    }

    public void run()
    {
        new Parser(this.lexer).buildAST();
    }
}
