package me.longerian.abc;

/**
 * Created by longerian on 16/5/29.
 */
public class NewClassTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("me.longerian.abc.NewClassSampleTest");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            clazz.newInstance();
        }
        long end = System.currentTimeMillis();
        System.out.println("newInstance() cost " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            new NewClassSampleTest();
        }
        end = System.currentTimeMillis();
        System.out.println("new() cost " + (end - start) + "ms");
    }

}
