package com.tk;

import javafx.util.Pair;

public class Parser
{
    private Lexer lexer;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
    }

    public void buildAST()
    {
        Pair<String, String> token;

        System.out.println("TOKENS:");

        do
        {
            token = lexer.getNextToken();

            // Print the token
            System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));
        }

        while(!token.getKey().equals("EOF"));

        System.out.println("----------------------------------------------------");
    }
}
