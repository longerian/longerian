package me.longerian.abcandroid.datetimepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/10/29.
 */
public class TestDateTimePickerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
//        ArrayMap<String, String> array = new ArrayMap<String, String>();
//        array.put("page", "shop/index");
//        array.put("userId", "420806631");
//        array.put("pageId", "339186");
//        for (int i = 0, size = array.size(); i < size; i++) {
//            final String name = array.keyAt(i);
//            final String value = array.valueAt(i);
//            Log.d("longer", "name = " + name + " value = " + value);
//        }

        new Thread() {
            @Override
            public void run() {
                // SSLContext方式进行SSL认证的客户端代码，把证书加载到 keystore，使用 keystore 里的 trustmanager 来校验
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
//                try {
//                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
//                    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
//                    InputStream caInput = new BufferedInputStream(getAssets().open("uwca.crt"));
//                    Certificate ca;
//                    try {
//                        ca = cf.generateCertificate(caInput);
//                        Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
//                        Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey().getFormat());
//                    } finally {
//                        caInput.close();
//                    }
//
//                    // Create a KeyStore containing our trusted CAs
//                    String keyStoreType = KeyStore.getDefaultType();
//                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//                    keyStore.load(null, null);
//                    keyStore.setCertificateEntry("ca", ca);
//
//                    // Create a TrustManager that trusts the CAs in our KeyStore
//                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//                    tmf.init(keyStore);
//
//                    // Create an SSLContext that uses our TrustManager
//                    SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
//                    context.init(null, tmf.getTrustManagers(), null);
//
//                    URL url = new URL("https://certs.cac.washington.edu/CAtest/");
//                    HttpsURLConnection urlConnection =
//                            (HttpsURLConnection)url.openConnection();
//                    urlConnection.setSSLSocketFactory(context.getSocketFactory());
////                    urlConnection.setHostnameVerifier(hostnameVerifier);
//                    InputStream in = urlConnection.getInputStream();
//                    copyInputStreamToOutputStream(in, System.out);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (KeyStoreException e) {
//                    e.printStackTrace();
//                } catch (KeyManagementException e) {
//                    e.printStackTrace();
//                } catch (NoSuchProviderException e) {
//                    e.printStackTrace();
//                }

                //========
                // SSLContext方式进行SSL认证的客户端代码，使用自定义的 trustmanager 来校验
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                    InputStream caInput = new BufferedInputStream(getAssets().open("uwca.crt"));
//                    InputStream caInput = new BufferedInputStream(getAssets().open("12306srca.cer"));
                    final Certificate ca;
                    try {
                        ca = cf.generateCertificate(caInput);
                        Log.i("Longer", "ca=" + ((X509Certificate) ca).getSubjectDN());
                        Log.i("Longer", "key=" + ((X509Certificate) ca).getPublicKey());
                    } finally {
                        caInput.close();
                    }
                    // Create an SSLContext that uses our TrustManager
                    SSLContext context = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
                    context.init(null, new TrustManager[]{
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(X509Certificate[] chain,
                                        String authType)
                                        throws CertificateException {

                                }

                                @Override
                                public void checkServerTrusted(X509Certificate[] chain,
                                        String authType)
                                        throws CertificateException {
                                    for (X509Certificate cert : chain) {
                                        Log.i("Longer", "Server Certificate Details:");
                                        Log.i("Longer", "---------------------------");
                                        Log.i("Longer", "IssuerDN: " + cert.getIssuerDN().toString());
                                        Log.i("Longer", "SubjectDN: " + cert.getSubjectDN().toString());
                                        Log.i("Longer", "Serial Number: " + cert.getSerialNumber());
                                        Log.i("Longer", "Version: " + cert.getVersion());
                                        Log.i("Longer", "Not before: " + cert.getNotBefore().toString());
                                        Log.i("Longer", "Not after: " + cert.getNotAfter().toString());
                                        Log.i("Longer", "local ca publicKey: " + ((X509Certificate) ca).getPublicKey().toString());
                                        Log.i("Longer", "---------------------------");

                                        // Make sure that it hasn't expired.
                                        cert.checkValidity();

                                        // Verify the certificate's public key chain.
                                        try {
                                            cert.verify(((X509Certificate) ca).getPublicKey());
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        } catch (InvalidKeyException e) {
                                            e.printStackTrace();
                                        } catch (NoSuchProviderException e) {
                                            e.printStackTrace();
                                        } catch (SignatureException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public X509Certificate[] getAcceptedIssuers() {
                                    return new X509Certificate[0];
                                }
                            }
                    }, null);

                    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            HostnameVerifier hv =
                                    HttpsURLConnection.getDefaultHostnameVerifier();
                            return "222.12306.cn".equals(hostname);
//                            return hv.verify("12306.cn", session);
                        }
                    };

                    URL url = new URL("https://certs.cac.washington.edu/CAtest/");
//                    URL url = new URL("https://www.12306.cn/");
                    HttpsURLConnection urlConnection =
                            (HttpsURLConnection)url.openConnection();
                    urlConnection.setSSLSocketFactory(context.getSocketFactory());
//                    urlConnection.setHostnameVerifier(hostnameVerifier);
                    InputStream in = urlConnection.getInputStream();
                    copyInputStreamToOutputStream(in, System.out);
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }

                //========
//                try {
//                    URL url = new URL("https://certs.cac.washington.edu/CAtest/");
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    copyInputStreamToOutputStream(in, System.out);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }.start();

    }

    private void copyInputStreamToOutputStream(InputStream in, PrintStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int c = 0;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
    }


}
