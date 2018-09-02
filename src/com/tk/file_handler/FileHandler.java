package com.tk.file_handler;

import java.io.*;

public class FileHandler
{
    public static String read(String name)
    {
        String content = "";

        try
        {
            FileReader reader = new FileReader(new File(name));
            BufferedReader buffReader = new BufferedReader(reader);

            String line;

            while((line = buffReader.readLine()) != null)
                content += line + "\n";
        }

        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        return content;
    }
}
