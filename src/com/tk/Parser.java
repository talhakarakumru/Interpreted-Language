package com.tk;

import com.tk.nodes.Call;
import com.tk.nodes.Echo;
import com.tk.nodes.Function;
import com.tk.nodes.Node;
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

                if(token.equals("EOF"))
                    return null;

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

        Interpreter.globalFunctionScope.add(f);

        return f;
    }

    private Echo echo(Node superNode) throws Exception
    {
        Pair<String, String> contentToken = checkNextToken(new String[]{ "STRING", "NUMBER", "ID" });

        if(contentToken == null)
            throw new Exception(error("There is no value for echo"));

        if(checkNextToken("NEW_LINE") == null)
            throw new Exception(error("Echos have to end with a new line"));

        return new Echo(superNode, contentToken.getValue());
    }

    private Node id(Node superNode, String value) throws Exception
    {
        if(checkNextToken("L_PARENT") != null)
            return call(superNode, value);

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
}
