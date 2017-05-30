package me.longerian.abc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by longerian on 15/8/5.
 */
public class RegExTest {

    public static void main(String[] args) {
        //test matach and replace
        Pattern pattern = Pattern.compile("([a-zA-Z]+[0-9]+)");
        Matcher matcher = pattern.matcher("ab12 cd efg34 asdf 123");

        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            System.out.println(matcher.group(1));
            matcher.appendReplacement(buffer, matcher.group(1));
            break;
        }
        System.out.println(matcher.group(1));
        matcher.appendTail(buffer);

        System.out.println(buffer.toString());

        //test case insensitive
        String str = "itemid itemId ItemId itemID id item_id";
        String regex = "(id|item_num_id|item_id|(?i)itemid)";

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        int count = matcher.groupCount();
        System.out.println("group count " + count);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }

        //test |
        String url = "http://gw.alicdn.com/tps/i1/TB11nZCHFXXXXasXFXXgMi7FVXX-27-84.png_120x120q90.jpg?_100x111";
        String sizeRex = "(\\d+)[x|-](\\d+)";
        pattern = Pattern.compile(sizeRex);
        matcher = pattern.matcher(url);
        while (matcher.find()) {
        count = matcher.groupCount();
        System.out.println("group count " + count);
            System.out.println(matcher.group(0) + " " + matcher.group(1) + " " + matcher.group(2));
        }

        int test = Integer.parseInt("00001001");
        System.out.println("result " + test);


        System.out.println("----->");
        Pattern mPattern = Pattern.compile("Home|Brand|MyTmall|Search|Shop");
        Matcher mMatcher = mPattern.matcher("Brand");
        System.out.println(mMatcher.matches());
        System.out.println("<-----");

        System.out.println(String.format("%.2f", 1.90877f));


    }

}
