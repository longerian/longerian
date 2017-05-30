package me.longerian.abc;

/**
 * Created by longerian on 15/4/23.
 * 提供统一的价格字符串描述信息
 */
public class TMPriceInfo {

    public final int unitStart = 0;

    public final int unitLength = 1;

    public final int integerStart = 1;

    public int integerLength = 0;

    public int dotStart = 0;

    public int dotLength = 0;

    public int decimalStart = 0;

    public int decimalLength = 0;

    public String unit = "￥";

    public String integerPart;

    public String dot = ".";

    public String decimalPart;

    public TMPriceInfo(String price) {
        if (price != null) {
            int dotIndex = price.indexOf(dot);
            if (dotIndex != -1) {
                dotStart = dotIndex + 1; //加上￥的位置
                dotLength = 1;
                integerPart = price.substring(0, dotIndex);
                integerLength = integerPart.length();
                decimalPart = price.substring(dotIndex + 1, price.length()).replaceAll("0+$", "");
                if (!"".equals(decimalPart)) {
                    decimalStart = dotStart + 1;
                    decimalLength = decimalPart.length();
                }
            } else {
                integerPart = price;
                integerLength = integerPart.length();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(unit);
        if (integerPart != null) {
            sb.append(integerPart);
        }
        if (decimalPart != null && !"".equals(decimalPart)) {
            sb.append(dot);
            sb.append(decimalPart);
        }
        return sb.toString();
    }

}
