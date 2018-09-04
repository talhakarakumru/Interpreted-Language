package com.tk;

import javafx.util.Pair;

import java.util.HashMap;

public class Lexer
{
    private HashMap<String, String> keywords;
    private String code;
    private int currentIndex;

    private Pair<String, String> currentToken;
    private int currentLine;

    public Lexer(String code)
    {
        this.code = code;
        this.currentIndex = 0;

        setKeywords();

        this.currentLine = 1;
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

    private String error(String msg)
    {
        return String.format("[ERROR]: %s at line %d.", msg, getCurrentLine());
    }

    public Pair<String, String> getNextToken() throws Exception
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

                else if(ch == '\n' && key.isEmpty() && temp.isEmpty())
                {
                    currentLine++;
                    return currentToken = new Pair<>("NEW_LINE", "new_line");
                }

                else if(ch != ' ')
                    throw new Exception(error("Invalid usage"));
            }

            else if(ch == '\"')
            {
                // String ends
                if(key.equals("STRING"))
                    return new Pair<>(key, temp);

                // String begins
                else if(temp.isEmpty())
                    key = "STRING";

                else throw new Exception(error("Invalid usage"));
            }

            // Left parenthesis
            else if(ch == '(' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("L_PARENT", "(");


            // Right parenthesis
            else if(ch == ')' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("R_PARENT", ")");

            // Colon
            else if(ch == ':' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("COLON", ":");

            // Equals
            else if(ch == '=' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("EQUALS", "=");

            // Greater
            else if(ch == '>' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("GREATER", ">");

             // Less
            else if(ch == '<' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("LESS", "<");

            // Not
            else if(ch == '!' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("NOT", "!");

            // Plus
            else if(ch == '+' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("PLUS", "+");

            // Minus
            else if(ch == '-' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("MINUS", "-");

            // Multiply
            else if(ch == '*' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("MULTIPLY", "*");

            // Divide
            else if(ch == '/' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("DIVIDE", "/");

            // Comma
            else if(ch == ',' && key.isEmpty() && temp.isEmpty())
                return currentToken = new Pair<>("COMMA", ",");

            // Number
            else if(Character.isDigit(ch) && key.isEmpty() && temp.isEmpty())
            {
                currentIndex--;
                return currentToken =new Pair<>("NUMBER", number());
            }

            else
            {
                temp += ch;

                // Keyword
                if(keywords.get(temp.toUpperCase()) != null && key.isEmpty())
                    return currentToken = new Pair<>(temp.toUpperCase(), temp.toUpperCase());

                // ID
                else if(checkNext(new char[]{ '\n', '(', ')', ':', '=', '>', '<', '!', '+', '-', '*', '/', ',' }, true) && !key.equals("STRING"))
                {
                    // Check if the id is invalid.
                    char nextChar = currentIndex < code.length() ? code.charAt(currentIndex + 1) : ' ';

                    if(nextChar != '\"')
                        return currentToken = new Pair<>("ID", temp);

                    else throw new Exception(error("Invalid id has been used"));
                }
            }
        }

        // End of the code.
        return currentToken = new Pair<>("EOF", "EOF");
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

    private boolean checkNext(char arr[], boolean ignoreWhitespace)
    {
        int index = 0;

        while(currentIndex + index < code.length())
        {
            if(code.charAt(currentIndex + index) == ' ' && ignoreWhitespace)
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

    //region Getters and Setters
    public int getCurrentLine()
    {
        return currentToken.getKey().equals("NEW_LINE") ? currentLine - 1 : currentLine;
    }
    //ednregion
}
