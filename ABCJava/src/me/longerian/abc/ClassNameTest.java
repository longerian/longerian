package me.longerian.abc;

/**
 * Created by longerian on 15/4/7.
 */
public class ClassNameTest {

    public static void main(String[] args) {
        ClassNameTest test = new ClassNameTest();
        System.out.println(test.getClass().getSimpleName());

        System.out.println(String.valueOf(12.44f));
        System.out.println(String.valueOf(12.40f));
        System.out.println(String.valueOf(12.00f));

        System.out.println(String.valueOf(Double.parseDouble("1111119.85")));
        System.out.println(String.valueOf(Double.parseDouble("123456789.85")));

        System.out.println(12.00f);
        System.out.println(Float.valueOf("12.00f"));

        PriceInfo info1 = new PriceInfo(String.valueOf(12.00f));
        System.out.println(info1);

        PriceInfo info2 = new PriceInfo(String.valueOf(12.40f));
        System.out.println(info2);

        PriceInfo info3 = new PriceInfo(String.valueOf(12.44f));
        System.out.println(info3);

        PriceInfo info4 = new PriceInfo(String.valueOf(12));
        System.out.println(info4);
    }

    private static class PriceInfo {

        public String integerPart;

        public String dot = ".";

        public String decimalPart;

        public PriceInfo(String price) {
            if (price != null) {
                int dotIndex = price.indexOf(dot);
                if (dotIndex != -1) {
                    integerPart = price.substring(0, dotIndex);
                    decimalPart = price.substring(dotIndex + 1, price.length());
                } else {
                    integerPart = price;
                }
            }
        }

        @Override
        public String toString() {
            return "PriceInfo{" +
                    "integerPart='" + integerPart + '\'' +
                    ", dot='" + dot + '\'' +
                    ", decimalPart='" + decimalPart + '\'' +
                    '}';
        }
    }

}
