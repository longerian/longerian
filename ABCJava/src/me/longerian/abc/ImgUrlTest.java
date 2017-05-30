package me.longerian.abc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by longerian on 16/3/17.
 */
public class ImgUrlTest {

    private static final Pattern REGEX_1 = Pattern.compile("(\\d+)x(\\d+)(_?q\\d+)?(\\.[jpg|png])");
    private static final Pattern REGEX_2 = Pattern.compile("(\\d+)-(\\d+)(_?q\\d+)?(\\.[jpg|png])");

    public static void main(String[] args) {
        float ratio = getImageRatio("http://img.alicdn.com/tps/i1/TB1x623LVXXXXXZXFXXzo_ZPXXX-372-441.png");
        System.out.println("ratio1 " + ratio);
        ratio = getImageRatio("http://img.alicdn.com/tps/i1/TB1P9AdLVXXXXa_XXXXzo_ZPXXX-372-441.png");
        System.out.println("ratio2 " + ratio);
        ratio = getImageRatio("http://img.alicdn.com/tps/i1/TB1NZxRLFXXXXbwXFXXzo_ZPXXX-372-441.png");
        System.out.println("ratio3 " + ratio);
        ratio = getImageRatio("http://img07.taobaocdn.com/tfscom/T10DjXXn4oXXbSV1s__105829.jpg_100x100.jpg");
        System.out.println("ratio4 " + ratio);
        ratio = getImageRatio("http://img07.taobaocdn.com/tfscom/T10DjXXn4oXXbSV1s__105829.jpg_100x100q90.jpg");
        System.out.println("ratio5 " + ratio);
        ratio = getImageRatio("http://img07.taobaocdn.com/tfscom/T10DjXXn4oXXbSV1s__105829.jpg_100x100q90.jpg_.webp");
        System.out.println("ratio6 " + ratio);
        ratio = getImageRatio("http://img03.taobaocdn.com/tps/i3/T1JYROXuRhXXajR_DD-1680-446.jpg_q50.jpg");
        System.out.println("ratio7 " + ratio);

        System.out.println("float " + (Float.NaN < 0));

    }

    public static float getImageRatio(String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0)
            return Float.NaN;

        try {
            Matcher matcher = REGEX_1.matcher(imageUrl);
            String widthStr;
            String heightStr;
            if (matcher.find()) {
                if (matcher.groupCount() >= 2) {
                    widthStr = matcher.group(1);
                    heightStr = matcher.group(2);
                    if (widthStr.length() < 5 && heightStr.length() < 5) {
                        int urlWidth = Integer.parseInt(widthStr);
                        int urlHeight = Integer.parseInt(heightStr);

                        if (urlWidth == 0 || urlHeight == 0) {
                            return 1;
                        }
                        return (float) urlWidth / urlHeight;
                    }
                }
            } else {
                matcher = REGEX_2.matcher(imageUrl);
                if (matcher.find()) {
                    if (matcher.groupCount() >= 2) {
                        widthStr = matcher.group(1);
                        heightStr = matcher.group(2);
                        if (widthStr.length() < 5 && heightStr.length() < 5) {
                            int urlWidth = Integer.parseInt(widthStr);
                            int urlHeight = Integer.parseInt(heightStr);

                            if (urlWidth == 0 || urlHeight == 0) {
                                return 1;
                            }
                            return (float) urlWidth / urlHeight;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Float.NaN;
    }

}
