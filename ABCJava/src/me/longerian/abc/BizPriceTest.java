package me.longerian.abc;

/**
 * Created by longerian on 16/3/7.
 */
public class BizPriceTest {

    public static void main(String[] args) {
        System.out.println(trimPrice("￥1000.00"));
        System.out.println(trimPrice("￥10034.00"));
        System.out.println(trimPrice("￥100.042"));
        System.out.println(trimPrice("￥100.0"));
        System.out.println(trimPrice("￥.fds100.0.090fd"));
        System.out.println(trimPrice("sfese;jsl3"));

        String test = "=";
        int index = test.indexOf("=");
        System.out.println(":" + test.substring(0, index));
        System.out.println(":" + test.substring(index + 1));

    }

    private static String trimPrice(String orgPrice) {
        String result = "";
        if (orgPrice != null) {
            int indexOfUnit = orgPrice.indexOf("￥");
            int indexOfDot = orgPrice.indexOf(".");
            if (indexOfDot < 0) {
                result = orgPrice;
            } else {
                int integerLength = indexOfDot - indexOfUnit - 1;
                int decimalLength = orgPrice.length() - indexOfDot - 1;
                if (indexOfUnit < 0) {
                    indexOfUnit = 0;
                }
                if (integerLength >= 4) {
                    result = orgPrice.substring(indexOfUnit, indexOfDot);
                } else {
                    if (decimalLength >= 2) {
                        result = orgPrice.substring(indexOfUnit, indexOfDot + 2);
                    } else {
                        result = orgPrice;
                    }
                }
            }
        }
        return result;
    }

}
