package com.tk;

import com.tk.nodes.*;
import javafx.util.Pair;

import java.lang.Boolean;
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
                    nodes.add(echo(superNode, false));

                else if(key.equals("PUT"))
                    nodes.add(echo(superNode, true));

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
                e.printStackTrace();
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

    private Echo echo(Node superNode, boolean hasNextLine) throws Exception
    {
        Pair<String, String> contentToken = checkNextToken(new String[]{ "STRING", "NUMBER", "TRUE", "FALSE", "ID", "MINUS" });
        String key = contentToken.getKey();
        String value = contentToken.getValue();

        if(contentToken == null)
            throw new Exception(error("There is no value for echo"));

        if(key.equals("ID") || key.equals("NUMBER") || key.equals("MINUS"))
            return new Echo(superNode, expr(superNode, null, contentToken), hasNextLine);

        else if(key.equals("TRUE") || key.equals("FALSE"))
            return new Echo(superNode, new MyBoolean(superNode, Boolean.parseBoolean(value)), hasNextLine);

        else
        {
            if(checkNextToken("NEW_LINE") == null)
                throw new Exception(error("Echos have to end with new line."));
        }

        return new Echo(superNode, new Text(value), hasNextLine);
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

        token = checkNextToken(new String[]{ "STRING", "NUMBER", "ID", "L_PARENT", "TRUE", "FALSE", "MINUS" });

        if(token != null)
        {
            String key = token.getKey();
            String value = token.getValue();

            if(key.equals("STRING"))
                return new Assignment(superNode, new Variable<String>(superNode, id), new Text(value));

            else if(key.equals("TRUE") || key.equals("FALSE"))
                return new Assignment(superNode, new Variable<Boolean>(superNode, id), new MyBoolean(superNode, Boolean.parseBoolean(value)));

            else if(key.equals("NUMBER") || key.equals("ID") || key.equals("L_PARENT") || key.equals("MINUS"))
                return new Assignment(superNode, new Variable<Double>(superNode, id), expr(superNode, null, token));
        }

        throw new Exception(error("Invalid assignment"));
    }

    private Expression expr(Node superNode, Node leftNode, Pair<String, String> tok) throws Exception
    {
        Pair<String, String> token = tok;
        String key;

        Node left, right = null;
        left = leftNode;

        char operation = '\0';

        do
        {
            if(token == null)
                token = checkNextToken(new String[]{ "NUMBER", "ID", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "L_PARENT", "R_PARENT", "NEW_LINE" });

            key = token.getKey();

            // Check if left, right and opetor is set.
            if(left != null && right != null && operation != '\0')
            {
                if(key.equals("MULTIPLY") || key.equals("DIVIDE"))
                {
                    if(operation == '+' || operation == '-')
                    {
                        Node r = expr(superNode, right, token);
                        return new Expression(superNode, left, r, operation);
                    }

                    else
                    {
                        left = new Expression(superNode, left, right, operation);
                        right = null;
                        operation = '\0';
                    }
                }

                else if(!key.equals("R_PARENT"))
                {
                    left = new Expression(superNode, left, right, operation);
                    right = null;
                    operation = '\0';
                }
            }

            if(key.equals("NEW_LINE"))
                break;

            else if(key.equals("NUMBER"))
            {
                if(left == null)
                    left = new Expression(Double.parseDouble(token.getValue()));

                else if(operation != '\0')
                    right = new Expression(Double.parseDouble(token.getValue()));

                else throw new Exception(error("Invalid expression"));
            }

            else if(key.equals("ID"))
            {
                if(left == null)
                    left = new Variable(superNode, token.getValue());

                else if(operation != '\0')
                    right = new Variable(superNode, token.getValue());

                else throw new Exception(error("Invalid expression"));
            }

            else if(key.equals("PLUS") || key.equals("MULTIPLY") || key.equals("DIVIDE"))
            {
                if(left != null)
                    operation = token.getValue().charAt(0);

                else throw new Exception(error("Invalid expression"));
            }

            else if(key.equals("MINUS"))
            {
                if(left != null)
                {
                    if(operation == '\0')
                        operation = token.getValue().charAt(0);

                    // Make right node negative or positive.
                    else if(operation == '*' || operation == '/')
                        left  = new Expression(superNode, left, new Expression(-1), '*');

                    else
                    {
                        right = expr(superNode, null, token);
                        break;
                    }
                }

                // Make left negative or positive.
                else
                {
                    left = new Expression(-1);
                    operation = '*';
                }
            }

            else if(key.equals("L_PARENT"))
            {
                if(left != null && operation != '\0')
                {
                    Node r = expr(superNode, null, null);
                    left = new Expression(superNode, left, r, operation);
                    right = null;
                    operation = '\0';
                }

                else left = expr(superNode, null, null);
            }

            else if(key.equals("R_PARENT"))
            {
                if(left != null && right != null && operation != '\0')
                    return new Expression(superNode, left, right, operation);

                else if(left != null && operation == '\0')
                    return new Expression(superNode, left);

                else throw new Exception(error("Invalid expression"));
            }

            else throw new Exception(error("Invalid expression"));

            token = checkNextToken(new String[]{ "NUMBER", "ID", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "L_PARENT", "R_PARENT", "NEW_LINE" });

            if(token == null)
                throw new Exception("Invalid expression.");
        }

        while(!token.getKey().equals("NEW_LINE"));

        if(left != null && right != null && operation != '\0')
            return new Expression(superNode, left, right, operation);

        else if(left != null && right == null && operation == '\0')
            return new Expression(superNode, left);

        else throw new Exception(error("Invalid expression"));

    }
}
