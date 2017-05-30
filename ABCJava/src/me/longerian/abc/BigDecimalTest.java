package me.longerian.abc;

import com.sun.jndi.toolkit.url.UrlUtil;
import sun.net.util.URLUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by longerian on 15/5/8.
 */
public class BigDecimalTest {

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("123456789.80");
        BigDecimal wbigDecimal = new BigDecimal("123456789.00");
        System.out.println(bigDecimal.toString());
        System.out.println(wbigDecimal.toString());

        System.out.println(bigDecimal.toString().replaceAll("0+$", ""));
        System.out.println(wbigDecimal.toString().replaceAll("0+$", ""));
        String ss = bigDecimal.toString().replaceAll("0+$", "");
        int index = ss.indexOf(".");
        System.out.println("index = " + index);
        System.out.println(ss.substring(index + 1, ss.length()).equals(""));


        System.out.println(new TMPriceInfo("156468769465543.32").toString());
        System.out.println(new TMPriceInfo("156468769465543.32").toString());
        System.out.println(new TMPriceInfo("12.000000").toString());
        System.out.println(new TMPriceInfo("12000000").toString());
        System.out.println(new TMPriceInfo(new BigDecimal(123456789797.43).toString()).toString());
        System.out.println(new TMSectionPriceInfo("232400.00", "54656.00000").toString());

//        RhinoScriptEngine js = new RhinoScriptEngine();
//        js.compile("");

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine rhino = scriptEngineManager.getEngineByName("rhino");
        String name = "Olli";
        try {
            rhino.eval("var a = 1; b = 2");
            rhino.eval("print(a + b)");
            rhino.eval("print('" + name + "')");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        String title = "1234567890";
        System.out.println(title);
        title = title.substring(0, title.length() - 1);
        System.out.println(title);
        title = title.substring(0, title.length() - 1);
        System.out.println(title);

        try {
            URI uri = new URI("//www.tmall.com");
            System.out.println(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}
