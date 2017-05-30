package me.longerian.abc;

/**
 * Created by longerian on 15/4/26.
 *
 * 提供统一的区间价格字符串描述信息
 */
public class TMSectionPriceInfo {

    public final int unitStart = 0;

    public final int unitLength = 1;

    public final int lowestIntegerStart = 1;

    public int lowestIntegerLength = 0;

    public int lowestDotStart = 0;

    public int lowestDotLength = 0;

    public int lowestDecimalStart = 0;

    public int lowestDecimalLength = 0;

    public int sectionStart = 0;

    public final int sectionLength = 1;

    public int highestIntegerStart = 0;

    public int highestIntegerLength = 0;

    public int highestDotStart = 0;

    public int highestDecimalStart = 0;

    public int highestDecimalLength = 0;

    public String section = "-";

    public String unit = "￥";

    public String dot = ".";

    public String lowestIntegerPart;

    public String lowestDecimalPart;

    public String highestIntegerPart;

    public String highestDecimalPart;

    public TMSectionPriceInfo(String lowestPrice, String highestPrice) {
        int cursor = 0;
        if (lowestPrice != null) {
            int dotIndex = lowestPrice.indexOf(dot);
            if (dotIndex != -1) {
                lowestDotStart = dotIndex + 1;//加上￥的位置
                lowestDotLength = 1;
                lowestIntegerPart = lowestPrice.substring(0, dotIndex);
                lowestIntegerLength = lowestIntegerPart.length();
                lowestDecimalPart = lowestPrice.substring(dotIndex + 1, lowestPrice.length()).replaceAll("0+$", "");
                if (!"".equals(lowestDecimalPart)) {
                    lowestDecimalStart = lowestDotStart + 1;
                    lowestDecimalLength = lowestDecimalPart.length();
                }
            } else {
                lowestIntegerPart = lowestPrice;
                lowestIntegerLength = lowestIntegerPart.length();
            }
        }
        sectionStart = unitLength + lowestIntegerLength + lowestDotLength + lowestDecimalLength;
        cursor = unitLength + lowestIntegerLength + lowestDotLength + lowestDecimalLength + sectionLength;
        if (highestPrice != null) {
            int dotIndex = highestPrice.indexOf(dot);
            if (dotIndex != -1) {
                highestDotStart = cursor + dotIndex;
                highestIntegerPart = highestPrice.substring(0, dotIndex);
                highestIntegerStart = cursor;
                highestIntegerLength = highestIntegerPart.length();
                highestDecimalPart = highestPrice.substring(dotIndex + 1, highestPrice.length()).replaceAll("0+$", "");
                if (!"".equals(highestDecimalPart)) {
                    highestDecimalStart = highestDotStart + 1;
                    highestDecimalLength = highestDecimalPart.length();
                }
            } else {
                highestIntegerStart = cursor;
                highestIntegerPart = highestPrice;
                highestIntegerLength = highestIntegerPart.length();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(unit);
        if (lowestIntegerPart != null) {
            sb.append(lowestIntegerPart);
        }
        if (lowestDecimalPart != null && !"".equals(lowestDecimalPart)) {
            sb.append(dot);
            sb.append(lowestDecimalPart);
        }
        sb.append(section);
        if (highestIntegerPart != null) {
            sb.append(highestIntegerPart);
        }
        if (highestDecimalPart != null && !"".equals(highestDecimalPart)) {
            sb.append(dot);
            sb.append(highestDecimalPart);
        }
        return sb.toString();
    }

}
