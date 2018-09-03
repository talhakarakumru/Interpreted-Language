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

    public LinkedList<Node> buildAST()
    {
        LinkedList<Node> ast = new LinkedList<>();
        Pair<String, String> token;

        Node superNode = null;

        System.out.println("TOKENS:");

        do
        {
            token = lexer.getNextToken();

            // Print the token
            System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

            String key = token.getKey();
            String value = token.getValue();

            if(key.equals("DEF"))
                ast.add(function(superNode));
        }

        while(!token.getKey().equals("EOF"));

        System.out.println("----------------------------------------------------");

        return ast;
    }

    private LinkedList<Node> subNodes(Node node)
    {
        LinkedList<Node> nodes = new LinkedList<>();
        Node superNode = node;

        Pair<String, String> token;

        do
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

        while(!token.getKey().equals("EOF"));

        return null;
    }

    private Pair<String, String> checkNextToken(String key)
    {
        Pair<String, String> token = lexer.getNextToken();

        System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

        if(token.getKey().equals(key))
            return token;

        return null;
    }

    private Pair<String, String> checkNextToken(String keys[])
    {
        Pair<String, String> token = lexer.getNextToken();

        System.out.println(String.format("\t{ \"%s\": \"%s\" }", token.getKey(), token.getValue()));

        for(String key : keys)
            if(token.getKey().equals(key))
                return token;

        return null;
    }

    private Function function(Node superNode)
    {
        Function f = null;

        Pair<String, String> idToken = checkNextToken("ID");
        assert idToken != null;

        assert checkNextToken("L_PARENT") != null;
        assert checkNextToken("R_PARENT") != null;
        assert checkNextToken("COLON") != null;

        f = new Function(superNode, idToken.getValue());
        f.setSubNodes(subNodes(f));

        Interpreter.globalFunctionScope.add(f);

        return f;
    }

    private Echo echo(Node superNode)
    {
        Pair<String, String> contentToken = checkNextToken(new String[]{ "STRING", "NUMBER", "ID" });

        assert contentToken != null;
        assert checkNextToken("NEW_LINE") != null;

        return new Echo(superNode, contentToken.getValue());
    }

    private Node id(Node superNode, String value)
    {
        if(checkNextToken("L_PARENT") != null)
            return call(superNode, value);

        return null;
    }

    private Call call(Node superNode, String id)
    {
        assert checkNextToken("RIGHT_PARENT") != null;
        assert checkNextToken("NEW_LINE") != null;

        return new Call(superNode, id);
    }
}
