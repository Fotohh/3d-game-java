package me.fotohh.javagame.log;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Map;

public final class Logger {

    static {
        AnsiConsole.systemInstall();
    }

    public static final char COLOR_SYMBOL = '&';

    public static void clearScreen(){
        Ansi.ansi().eraseScreen();
    }

    public synchronized static void print(String message){

        String msg = parseString(message);
        String a = Ansi.ansi().render(msg).toString();

        Ansi n = null;
        if(a.contains("@|") && a.contains("|@")) {
            n = Ansi.ansi().render(a);
            while(n.toString().contains("@|") && n.toString().contains("|@")){
                n = Ansi.ansi().render(n.toString());
            }
        }
        if(n != null) a = n.toString();

        System.out.println(a);

    }

    private synchronized static String parseString(String s){
        final StringBuilder builder = new StringBuilder(s);
        int size = builder.length();
        boolean searching = false;
        boolean bold = false;
        int boldCount = 0;
        String recentSymbol = null;

        for(int i = 0; i < size; i++){

            char character = builder.charAt(i);

            if(character == COLOR_SYMBOL){

                if (i + 1 >= size - 1) break;

                String currentChar = String.valueOf(builder.charAt(i + 1));

                String colorSymbol = keys.getOrDefault(currentChar, null);
                if(colorSymbol == null) continue;

                if(bold && colorSymbol.equals("reset")) {
                    bold = false;
                    boldCount--;
                    if(!recentSymbol.equals("bold")) builder.insert(i, "|@");
                    builder.insert(i, "|@");
                    searching = false;
                    continue;
                }
                if(searching){
                    if(!bold) {
                        builder.insert(i, "|@");
                        searching = false;
                        continue;
                    }
                    if(!recentSymbol.equals("bold")) {
                        builder.insert(i, "|@");
                        searching = false; continue;
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
                    if(currentChar.equals("l")) bold = true;
                    recentSymbol = colorSymbol;
                    if(colorSymbol.equals("bold")){
                        boldCount ++;
                    }
                }
                continue;
            }

            if(i == size-1 && searching){
                builder.insert(builder.length(), "|@");
            }
            if(i == size-1){
                if(boldCount > 0){
                    char c1 = builder.charAt(builder.length()-1);
                    char c2 = builder.charAt(builder.length()-2);
                    if(c1 == '@' && c2 == '|'){
                        boldCount--;
                    }
                    while(boldCount > 0) {
                        builder.insert(builder.length(), "|@");
                        boldCount--;
                    }
                }
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
        print("&e[&l&4SEVERE&r&e]&r  "+message);
    }

    public static void warn(String message){
        print("&e[&4WARN&e]&r  "+message);
    }

    public static void debug(String message){
        print("&e[&1DEBUG&e]&r  "+message);
    }

    public static void lethal(String message){
        print("&4[&lLETHAL&r&4]&r  "+message);
    }

}
