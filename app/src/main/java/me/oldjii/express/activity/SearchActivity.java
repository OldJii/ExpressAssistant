package me.oldjii.express.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.zxing.activity.CaptureActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.oldjii.express.R;
import me.oldjii.express.constants.Extras;
import me.oldjii.express.constants.RequestCode;
import me.oldjii.express.http.HttpCallback;
import me.oldjii.express.http.HttpClient;
import me.oldjii.express.model.CompanyEntity;
import me.oldjii.express.model.SearchInfo;
import me.oldjii.express.model.SuggestionResult;
import me.oldjii.express.utils.PermissionReq;
import me.oldjii.express.utils.SnackbarUtils;
import me.oldjii.express.utils.binding.Bind;
import me.oldjii.express.viewholder.SuggestionViewHolder;
import me.oldjii.express.widget.radapter.RAdapter;
import me.oldjii.express.widget.radapter.RSingleDelegate;

public class SearchActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    @Bind(R.id.et_post_id)
    private EditText etPostId;
    //右侧扫描按钮
    @Bind(R.id.iv_scan)
    private ImageView ivScan;
    //右侧清空文本按钮
    @Bind(R.id.iv_clear)
    private ImageView ivClear;
    @Bind(R.id.rv_suggestion)
    private RecyclerView rvSuggestion;

    private Map<String, CompanyEntity> companyMap = new HashMap<>();
    private List<CompanyEntity> suggestionList = new ArrayList<>();
    private RAdapter<CompanyEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        readCompany();

        etPostId.addTextChangedListener(this);
        ivScan.setOnClickListener(this);
        ivClear.setOnClickListener(this);

        adapter = new RAdapter<>(suggestionList, new RSingleDelegate<>(SuggestionViewHolder.class));       //SuggestionViewHolder中实现快递公司的点击事件
        rvSuggestion.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestion.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//添加分割线
        rvSuggestion.setAdapter(adapter);
    }

    private void readCompany() {
        try {
            /**
             *读取本地文件里的快递公司列表，将数据导到companyMap中，以便以后使用
             *
             */
            InputStream is = getAssets().open("company.json");
            //company.json在main目录下的assets目录下,getAssets()用于获取assets目录下的资源文件
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);       //将本地json转换为String

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();     //将JSON的String 转成一个JsonArray对象

            //加强for循环遍历JsonArray，JsonElement是一个抽象类，代表 JSON 串中的某一个元素，可以是 JsonObject/JsonArray/JsonPrimitive/… 中的任何一种元素
            for (JsonElement obj : jArray) {
                CompanyEntity company = gson.fromJson(obj, CompanyEntity.class);    //利用Gson将json数据转换为CompanyEntity.class类型
                if (!TextUtils.isEmpty(company.getCode())) {
                    companyMap.put(company.getCode(), company);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(final Editable s) {
        if (s.length() > 0) {
            ivScan.setVisibility(View.INVISIBLE);
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivScan.setVisibility(View.VISIBLE);
            ivClear.setVisibility(View.INVISIBLE);
        }
        suggestionList.clear();
        adapter.notifyDataSetChanged();//刷新页面
        if (s.length() >= 8) {
            getSuggestion(s.toString());
        }
        adapter.setTag(s.toString());
    }

    private void getSuggestion(final String postId) {
        HttpClient.getSuggestion(postId, new HttpCallback<SuggestionResult>() {
            @Override
            public void onResponse(SuggestionResult suggestionResult) {
                if (!TextUtils.equals(etPostId.getText().toString(), postId)) {
                    return;
                }
                onSuggestion(suggestionResult);
            }

            @Override
            public void onError(VolleyError volleyError) {
                if (!TextUtils.equals(etPostId.getText().toString(), postId)) {
                    return;
                }
                onSuggestion(null);
            }
        });
    }

    //提示物流公司显示在布局上的具体实现
    private void onSuggestion(SuggestionResult response) {
        suggestionList.clear();
        if (response != null && response.getAuto() != null && !response.getAuto().isEmpty()) {
            for (SuggestionResult.AutoBean bean : response.getAuto()) {
                if (companyMap.containsKey(bean.getComCode())) {
                    suggestionList.add(companyMap.get(bean.getComCode()));
                }
            }
        }
        String label = "<font color='%1$s'>没有查到？</font> <font color='%2$s'>请选择快递公司</font>";
        String grey = String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.grey));
        String blue = String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.blue));
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(String.format(label, grey, blue));
        suggestionList.add(companyEntity);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scan:
                startCaptureActivity();
                break;
            case R.id.iv_clear:
                etPostId.setText("");
                break;
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Manifest.permission.CAMERA)
                .result(new PermissionReq.Result() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(SearchActivity.this, true, RequestCode.REQUEST_CAPTURE);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(SearchActivity.this, "没有相机权限，无法扫描单号！");
                    }
                })
                .request();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        switch (requestCode) {
            case RequestCode.REQUEST_CAPTURE:
                // 处理扫描结果（在界面上显示）
                String resultStr = data.getStringExtra(Extras.SCAN_RESULT);
                etPostId.setText(resultStr);
                etPostId.setSelection(etPostId.length());
                break;
            case RequestCode.REQUEST_COMPANY:
                SearchInfo mSearchInfo = (SearchInfo) data.getSerializableExtra(Extras.SEARCH_INFO);
                mSearchInfo.setPost_id(etPostId.getText().toString());
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra(Extras.SEARCH_INFO, mSearchInfo);
                startActivity(intent);
                break;
        }
    }
}
