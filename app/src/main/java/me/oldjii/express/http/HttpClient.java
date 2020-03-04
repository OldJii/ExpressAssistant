package me.oldjii.express.http;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.oldjii.express.application.ExpressApplication;
import me.oldjii.express.model.SearchResult;
import me.oldjii.express.model.SuggestionResult;

/**
 * Created by oldjii on 2019/4
 */
//使用Volley
public class HttpClient {
    private static final String BASE_URL = "https://www.kuaidi100.com";
    private static final String HEADER_REFERER = "Referer";

    private static RequestQueue mRequestQueue;

    //信任所有https证书
    static {
        FakeX509TrustManager.allowAllSSL();
    }

    private static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ExpressApplication.getInstance().getApplicationContext());
        }
        return mRequestQueue;
    }

    public static void query(String type, String postId, final HttpCallback<SearchResult> callback) {
        /**
         * 第一步：做好URL
         */
        String appcode = "5a7bb7fc739c44c5a5c345d7bf41a9c2";
        String action = "http://kuaidi100.market.alicloudapi.com/getExpress";
        Map<String, String> params = new HashMap<>(2);
        params.put("NO", postId);
        params.put("TYPE", type);
        String url = makeUrli(action, params);
        /**
         * 第二步：创建GsonRequest，传入URL，参数，并重写getHeaders()
         */
        GsonRequest<SearchResult> request = new GsonRequest<SearchResult>(url, SearchResult.class,
                searchResult -> callback.onResponse(searchResult),
                volleyError -> callback.onError(volleyError)) {
            @Override
            public Map<String, String> getHeaders() {
                //TODO:
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "APPCODE " + appcode);
                return headers;
            }
        };

        /**
         * 第三步：将Gsonrequest传入RequestQueue
         */
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    /**
     * 输入单号之后，根据单号显示可选择的物流公司
     *
     * url形式有两种
     *  -https://www.kuaidi100.com/autonumber/autoComNum?text=单号
     *  --这种是原app使用，但没有在快递100官方api文档发现该形式
     *  -http://www.kuaidi100.com/autonumber/auto?num=单号
     *  --这种是api文档中提供的形式，但是文档中有key值，经尝试不加key值也可查值
     *  --原url - http://www.kuaidi100.com/autonumber/auto?num=[单号]&key=[key]
     *
     * 单号
     * @param postId
     */
    public static void getSuggestion(final String postId, final HttpCallback<SuggestionResult> callback) {
        String action = "/autonumber/autoComNum";
        Map<String, String> params = new HashMap<>(1);
        params.put("text", postId);
        String url = makeUrl(action, params);

        GsonRequest<SuggestionResult> request = new GsonRequest<SuggestionResult>(Request.Method.POST, url, SuggestionResult.class,
                new Response.Listener<SuggestionResult>() {
                    @Override
                    public void onResponse(SuggestionResult response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(HEADER_REFERER, BASE_URL);
                return headers;
            }
        };
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    /**
     * 快递公司的logo图标在api中获取，url为
     *  - http://www.kuaidi100.com//images/all/56/huitongkuaidi.png
     *
     * @param logo
     * @return
     */
    public static String urlForLogo(String logo) {
        String action = "/images/all/" + logo;
        return makeUrl(action, null);
    }

    private static String makeUrl(String action, Map<String, String> params) {
        String url = BASE_URL + action;
        if (params == null || params.isEmpty()) {
            return url;
        }

        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                continue;
            }

            url += (i == 0) ? "?" : "&";
            url += (entry.getKey() + "=" + URLEncoder.encode(entry.getValue()));
            i++;
        }
        return url;
    }

    /**
     * 新建的用户新url的工厂方法
     * @param action
     * @param params
     * @return
     */
    private static String makeUrli(String action, Map<String, String> params) {
        String url = action;
        if (params == null || params.isEmpty()) {
            return url;
        }

        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                continue;
            }

            url += (i == 0) ? "?" : "&";
            url += (entry.getKey() + "=" + URLEncoder.encode(entry.getValue()));
            i++;
        }
        return url;
    }
}
