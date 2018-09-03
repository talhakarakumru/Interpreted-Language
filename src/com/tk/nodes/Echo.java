package com.tk.nodes;

public class Echo extends Node
{
    private String content;

    public Echo(Node superNode, String content)
    {
        super(Type.ECHO, superNode);

        this.content = content;
    }

    @Override
    public void run()
    {
        System.out.println(content);
    }

    //region Getters and Setters
    public String getContent()
    {
        return content;
    }
    //endregion
}
