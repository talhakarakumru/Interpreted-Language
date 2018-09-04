package com.tk;

import com.tk.file_handler.FileHandler;

public class Main
{
    public static void main(String[] args)
    {
        if(args.length > 1)
            new Interpreter(FileHandler.read(args[2])).run();

        new Interpreter(FileHandler.read("Main.tk")).run();
    }
}
