package me.oldjii.express.http;

import com.android.volley.VolleyError;

/**
 * Created by oldjii on 2019/4
 */
public interface HttpCallback<T> {
    void onResponse(T t);

    void onError(VolleyError volleyError);
}
