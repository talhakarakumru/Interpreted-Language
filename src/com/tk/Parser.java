package com.tk;

import com.tk.nodes.*;
import javafx.util.Pair;

import java.util.LinkedList;

public class Parser
{
    private Lexer lexer;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
    }

    private String error(String msg)
    {
        return String.format("[ERROR]: %s at line %d.", msg, lexer.getCurrentLine());
    }

    public LinkedList<Node> buildAST()
    {
        LinkedList<Node> ast = new LinkedList<>();
        Pair<String, String> token = null;

        System.out.println("TOKENS:");

        do
        {
            try
            {
                token = lexer.getNextToken();

                // Print the token
                System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

                String key = token.getKey();
                String value = token.getValue();

                if (key.equals("DEF"))
                    ast.add(function(null));

                else if(key.equals("ID"))
                    ast.add(id(null, value));

                else if(!key.equals("NEW_LINE") && !key.equals("EOF"))
                    throw new Exception(error("Invalid usage"));
            }


            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

        while(!token.getKey().equals("EOF"));

        System.out.println("----------------------------------------------------");

        return ast;
    }

    private LinkedList<Node> subNodes(Node node)
    {
        LinkedList<Node> nodes = new LinkedList<>();
        Node superNode = node;

        Pair<String, String> token = null;

        do
        {
            try
            {
                token = lexer.getNextToken();

                // Print the token
                System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

                String key = token.getKey();
                String value = token.getValue();

                if(key.equals("ECHO"))
                    nodes.add(echo(superNode));

                else if(key.equals("ID"))
                    nodes.add(id(superNode, value));

                else if(key.equals("END"))
                {
                    if(!superNode.equals(node))
                        superNode = superNode.getSuperNode();

                    else return nodes;
                }

                else if(!key.equals("NEW_LINE") && !key.equals("EOF"))
                    throw new Exception(error("Invalid usage"));
            }

            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

        while(!token.getKey().equals("EOF"));

        return null;
    }

    private Pair<String, String> checkNextToken(String key) throws Exception
    {
        Pair<String, String> token = lexer.getNextToken();

        System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

        if(token.getKey().equals(key))
            return token;

        return null;
    }

    private Pair<String, String> checkNextToken(String keys[]) throws Exception
    {
        Pair<String, String> token = lexer.getNextToken();

        System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

        for(String key : keys)
            if(token.getKey().equals(key))
                return token;

        return null;
    }

    private Function function(Node superNode) throws Exception
    {
        Function f = null;

        Pair<String, String> idToken = checkNextToken("ID");

        if(idToken == null)
            return null;

        if(checkNextToken("L_PARENT") == null)
            throw new Exception(error("Missing left parenthesis"));

        if(checkNextToken("R_PARENT") == null)
            throw new Exception(error("Missing right parenthesis"));

        if(checkNextToken("COLON") == null)
            throw new Exception(error("Missing colon"));

       if(checkNextToken("NEW_LINE") == null)
           throw new Exception(error("Functions have to end with a new line"));

        f = new Function(superNode, idToken.getValue());
        f.setSubNodes(subNodes(f));

        // Check if function does not already exist.
        if(!Interpreter.globalFunctionScope.contains(f))
            Interpreter.globalFunctionScope.add(f);

        else throw new Exception("You cannot have same function.");

        return f;
    }

    private Echo echo(Node superNode) throws Exception
    {
        Pair<String, String> contentToken = checkNextToken(new String[]{ "STRING", "NUMBER", "TRUE", "FALSE", "ID" });
        String key = contentToken.getKey();
        String value = contentToken.getValue();

        if(contentToken == null)
            throw new Exception(error("There is no value for echo"));

        if(checkNextToken("NEW_LINE") == null)
            throw new Exception(error("Echos have to end with a new line"));

        if(key.equals("ID"))
            return new Echo(superNode, new Variable(value));

        return new Echo(superNode, new Text(value));
    }

    private Node id(Node superNode, String value) throws Exception
    {
        Pair<String, String> token = checkNextToken(new String[]{ "L_PARENT", "EQUALS" });

        if(token != null)
        {
            String key = token.getKey();

            // Function call.
            if(key.equals("L_PARENT"))
                return call(superNode, value);

            // Check if an assignment or a statement.
            else if(key.equals("EQUALS"))
            {
                // Statement
                if(lexer.checkNext(new char[]{ '=' }, true));

                // Assignment
                else return assignment(superNode, value);
            }

            else throw new Exception(error("Invalid id usage"));
        }

        return null;
    }

    private Call call(Node superNode, String id) throws Exception
    {
        if(checkNextToken("R_PARENT") == null)
            throw new Exception(error("Missing right parenthesis"));

        if(checkNextToken("NEW_LINE") == null)
            throw new Exception(error("Function calls have to end with new line"));

        return new Call(superNode, id);
    }

    private Assignment assignment(Node superNode, String id) throws Exception
    {
        Pair<String, String> token;

        token = checkNextToken(new String[]{ "STRING", "NUMBER", "BOOLEAN" });


        // TODO: This sets variables as string. It is not good.
        if(token != null)
        {
            String key = token.getKey();
            String value = token.getValue();

            if(key.equals("STRING") || key.equals("BOOLEAN"))
                return new Assignment(superNode, new Variable<String>(id), new Text(value));

            if(key.equals("NUMBER"))
                return new Assignment(superNode, new Variable<Double>(id), new Expression(Double.parseDouble(value)));
    }

        throw new Exception(error("Invalid assignment"));
    }
}
