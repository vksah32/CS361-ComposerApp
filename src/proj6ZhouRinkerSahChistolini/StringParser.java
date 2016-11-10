package proj6ZhouRinkerSahChistolini;

import java.util.ArrayList;
import java.util.regex.*;

/**
 * Created by vivek on 11/9/16.
 */
public class StringParser {


    public static ArrayList<String>  Parse(String s){
        ArrayList<String> result = new ArrayList<>();
        int firstIndex = s.indexOf('{');
        int lastIndex = s.lastIndexOf('}');
        if (firstIndex != -1){
            String k = s.substring(firstIndex+1, lastIndex);
            result.add(k);
            result.addAll(Parse(k));
            return result;
        } else {
            return  new ArrayList<>();

        }






    }


    public static void main(String[] args) {
        ArrayList<String> s = StringParser.Parse("h{ jv { hv { hjvh } iugub } hj }b");

        for (String st:s){
            System.out.println(st);
        }
    }
}
