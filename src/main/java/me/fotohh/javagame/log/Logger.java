package me.fotohh.javagame.log;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Map;

public final class Logger {

    public void main() {
        print("&l&4 Hello World!");
        /*info("Hello World");
        severe("Hello World");
        warn("Hello World");
        lethal("Hello World");
        debug("Hello World");*/
    }

    public static final char COLOR_SYMBOL = '&';

    public static void clearScreen(){
        Ansi.ansi().eraseScreen();
    }

    public static void print(String message){

        AnsiConsole.systemInstall();

        String msg = parseString(message);
        String a = Ansi.ansi().render(msg).toString();
        Ansi n = Ansi.ansi().render(a);

        System.out.println(n);

        AnsiConsole.systemUninstall();
    }

    private static String parseString(String s){
        final StringBuilder builder = new StringBuilder(s);
        int size = builder.length();
        boolean searching = false;
        boolean bold = false;

        for(int i = 0; i < size; i++){

            char character = builder.charAt(i);

            if(character == COLOR_SYMBOL){

                if (i + 1 >= size - 1) break;

                String currentChar = String.valueOf(builder.charAt(i + 1));

                String colorSymbol = keys.getOrDefault(currentChar, null);
                if(colorSymbol == null) continue;

                if(currentChar.equals("l")) bold = true;

                if(bold && colorSymbol.equals("reset")) {
                    bold = false;
                    builder.insert(i, "|@");
                    continue;
                }
                if(searching){
                    if(!bold) {
                        builder.insert(i, "|@");
                        continue;
                    }
                    searching = false;
                }
                if(!searching){
                    if(i + 2 >= size-1) break;
                    searching = true;
                    builder.setCharAt(i, '@');
                    builder.setCharAt(i + 1, '|');
                    if(String.valueOf(builder.charAt(i+2)).equals(" ")) {
                        builder.insert(i + 2, colorSymbol);
                    }else{
                        builder.insert(i + 2, colorSymbol + " ");
                    }
                    size = builder.length();
                }
                continue;
            }

            if(i == size-1 && searching){
                builder.insert(builder.length(), "|@");
            }
            if(i == size-1){
                if(bold) builder.insert(builder.length(), "|@");
            }

        }
        
        return builder.toString();
    }

    public static final Map<String, String> keys = Map.of(
            "f", "white",
            "1", "blue",
            "2", "green",
            "3", "cyan",
            "4", "red",
            "5", "purple",
            "e", "yellow",
            "d", "magenta",
            "l", "bold",
            "r", "reset"
    );

    public static void info(String message){
        print("&4[&eINFO&4]&r  "+ message);
    }

    public static void severe(String message){
        print("&e[&l&4SEVERE&e]&r  "+message);
    }

    public static void warn(String message){
        print("&e[&4WARN&e]&r  "+message);
    }

    public static void debug(String message){
        print("&e[&1DEBUG&e]&r  "+message);
    }

    public static void lethal(String message){
        print("&4[&lLETHAL&4]&r  "+message);
    }

}
