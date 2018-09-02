package com.tk;

import com.tk.file_handler.FileHandler;

public class Main
{
    public static void main(String[] args)
    {
        new Interpreter(FileHandler.read("Main.tk")).run();
    }
}
