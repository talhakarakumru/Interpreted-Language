package com.tk.nodes;

import java.util.LinkedList;

public abstract class Node
{
    protected Node superNode;
    protected LinkedList<Node> subNodes;

    public Node(Node superNode)
    {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();
    }

    abstract public void run() throws Exception;

    public void addSubNode(Node node)
    {
        this.subNodes.add(node);
    }

    //region Getters and Setters
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
}
