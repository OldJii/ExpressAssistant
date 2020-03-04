# 快递助手
![](https://raw.githubusercontent.com/OldJii/ExpressAssistant/master/app/src/main/res/drawable-xxhdpi/ic_launcher.png)

## 简介
这是一款超轻巧的查询快递的app，仅1M。<br>
支持包含申通、中通、圆通、EMS等热门物流公司在内的全国近多家公司，支持扫码查询，智能识别快递公司。<br>

## 项目
### 开源技术
- ZXing、Volley、ormlite、Gson、Glide

### 公开API
- 快递查询：阿里云API服务

### 关键代码
```java
public static void query(String type, String postId, final HttpCallback<SearchResult> callback) {
        String appcode = "***";
        String action = "http://kuaidi100.market.alicloudapi.com/getExpress";
        Map<String, String> params = new HashMap<>(2);
        params.put("NO", postId);
        params.put("TYPE", type);
        String url = makeUrli(action, params);
        GsonRequest<SearchResult> request = new GsonRequest<SearchResult>(url, SearchResult.class,
                searchResult -> callback.onResponse(searchResult),
                volleyError -> callback.onError(volleyError)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "APPCODE " + appcode);
                return headers;
            }
        };
```

## 作者
博客：jiguankai.cn<br>
邮箱：mail@jiguankai.cn

## License

     Copyright (C) 2019 OldJii
    
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
    
          http://www.apache.org/licenses/LICENSE-2.0
    
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
