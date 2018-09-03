package com.tk;

import com.tk.nodes.Function;
import com.tk.nodes.Node;

import java.util.LinkedList;

public class Interpreter
{
    private Lexer lexer;

    public static LinkedList<Variable> globalVarScope;
    public static LinkedList<Function> globalFunctionScope;

    public Interpreter(String code)
    {
        this.lexer = new Lexer(code);

        this.globalVarScope = new LinkedList<>();
        this.globalFunctionScope = new LinkedList<>();
    }

    public void run()
    {
        LinkedList<Node> ast = new Parser(this.lexer).buildAST();

        System.out.println("OUTPUT: ");

        for(Node node : ast)
        {
//            System.out.println(node.getType() + ": " + node.getSubNodes().size());

            if(node.getType() == Node.Type.FUNCTION)
            {
                if(((Function) node).getId().equals("main"))
                    node.run();
            }

            else node.run();
        }

        System.out.println("----------------------------------------------------");
    }
}
