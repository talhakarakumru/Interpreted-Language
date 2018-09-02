package com.tk;

import javafx.util.Pair;

import java.util.HashMap;

public class Lexer
{
    private HashMap<String, String> keywords;
    private String code;
    private int currentIndex;

    public Lexer(String code)
    {
        this.code = code;
        this.currentIndex = 0;

        setKeywords();
    }

    private void setKeywords()
    {
        keywords = new HashMap<>();

        keywords.put("DEF", "def");
        keywords.put("END", "end");
        keywords.put("ECHO", "echo");
        keywords.put("IF", "if");
        keywords.put("ELIF", "elif");
        keywords.put("ELSE", "else");
        keywords.put("AND", "and");
        keywords.put("OR", "or");
    }

    public Pair<String, String> getNextToken()
    {
        String key = "";
        String temp = "";

        while(currentIndex < code.length())
        {
            char ch = code.charAt(currentIndex++);

            if(ch == ' ' || ch == '\n')
            {
                if(key.equals("STRING"))
                    temp += ch;
            }

            else if(ch == '\"')
            {
                // String ends
                if(key.equals("STRING"))
                    return new Pair<>(key, temp);

                // String begins
                else if(temp.isEmpty())
                    key = "STRING";
            }

            // Left parenthesis
            else if(ch == '(' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("L_PARENT", "(");


            // Right parenthesis
            else if(ch == ')' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("R_PARENT", ")");

            // Colon
            else if(ch == ':' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("COLON", ":");

            // Equals
            else if(ch == '=' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("EQUALS", "=");

            // Greater
            else if(ch == '>' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("GREATER", ">");

             // Less
            else if(ch == '<' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("LESS", "<");

            // Not
            else if(ch == '!' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("NOT", "!");

            // Plus
            else if(ch == '+' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("PLUS", "+");

            // Minus
            else if(ch == '-' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("MINUS", "-");

            // Multiply
            else if(ch == '*' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("MULTIPLY", "*");

            // DIVIDE
            else if(ch == '/' && key.isEmpty() && temp.isEmpty())
                return new Pair<>("DIVIDE", "/");

            // Number
            else if(Character.isDigit(ch) && key.isEmpty() && temp.isEmpty())
            {
                currentIndex--;
                return new Pair<>("NUMBER", number());
            }

            else
            {
                temp += ch;

                // Keyword
                if(keywords.get(temp.toUpperCase()) != null && key.isEmpty())
                    return new Pair<>(temp.toUpperCase(), temp.toUpperCase());

                // ID
                else if(checkNext(new char[]{ '\n', '\"', '(', ')', ':', '=', '>', '<', '!', '+', '-', '*', '/' }) && !key.equals("STRING"))
                    return new Pair<>("ID", temp);
            }
        }

        // End of the code.
        return new Pair<>("EOF", "EOF");
    }

    private String number()
    {
        String num = "";
        char ch = code.charAt(currentIndex);
        boolean dotUsed = false;

        while(Character.isDigit(ch) || ch == '.')
        {
            if(ch == '.' && !dotUsed)
                num += ch;

            else if(Character.isDigit(ch))
                num += ch;

            ch = code.charAt(++currentIndex);
        }

        return num;
    }

    private boolean checkNext(char arr[])
    {
        int index = 0;

        while(currentIndex + index < code.length())
        {
            if(code.charAt(currentIndex + index) == ' ')
                index++;

            else
            {
                for(char ch : arr)
                {
                    if(code.charAt(currentIndex + index) == ch)
                        return true;
                }

                return false;
            }
        }

        return false;
    }
}
