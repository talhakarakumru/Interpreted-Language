package com.tk;

import com.tk.nodes.Function;
import com.tk.nodes.Node;
import com.tk.nodes.Variable;

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

            try
            {
                if(node instanceof Function)
                {
                    if(((Function) node).getId().equals("main"))
                        node.run();
                }

                else node.run();
            }

            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

        System.out.println("----------------------------------------------------");
    }

    public static Variable findVariable(Node superNode, String varId)
    {
        // Try to find it in its super nodes.
        while(superNode != null)
        {
            if(superNode instanceof Function)
            {
                Function f = (Function) superNode;

                for(Variable var : f.getVarScope())
                {
                    if(var.equals(new Variable(superNode, varId)))
                        return var;
                }
            }

            superNode = superNode.getSuperNode();
        }

        // Check if global scope has it.
        for(Variable var : globalVarScope)
        {
            if(var.equals(new Variable(superNode, varId)))
                return var;
        }

        return null;
    }

    public static boolean isConstant(Variable var)
    {
        for(char ch : var.getId().toCharArray())
            if(Character.isLowerCase(ch))
                return false;

        return true;
    }
}
