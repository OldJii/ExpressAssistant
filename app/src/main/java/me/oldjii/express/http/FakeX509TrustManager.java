package me.oldjii.express.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 信任所有Https证书，使Volley支持所有Https请求。
 * 该代码源于网络
 *
 * Created by oldjii on 2019/4
 */
public class FakeX509TrustManager implements X509TrustManager {
    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }
}