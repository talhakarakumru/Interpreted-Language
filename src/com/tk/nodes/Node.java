package com.tk.nodes;

import java.util.LinkedList;

public abstract class Node
{
    protected Type type;
    protected Node superNode;
    protected LinkedList<Node> subNodes;

    public Node(Type type, Node superNode)
    {
        this.type = type;
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();
    }

    abstract public void run() throws Exception;

    public void addSubNode(Node node)
    {
        this.subNodes.add(node);
    }

    //region Getters and Setters
    public Type getType()
    {
        return type;
    }

    public Node getSuperNode()
    {
        return superNode;
    }

    public LinkedList<Node> getSubNodes()
    {
        return subNodes;
    }

    public void setSubNodes(LinkedList<Node> subNodes)
    {
        this.subNodes = subNodes;
    }
    //endregion

    public enum Type
    {
        FUNCTION,
        ECHO,
        CALL,
        ASSIGNMENT,
        VARIABLE,
        EXPRESSION,
        TEXT,
        BOOLEAN
    }
}
