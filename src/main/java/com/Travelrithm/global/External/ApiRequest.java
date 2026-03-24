package com.Travelrithm.global.External;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public interface ApiRequest{
    static GetBuilder get(String baseUrl) {
        return new GetBuilder(baseUrl);
    }
    static PostBuilder post(String baseUrl) {
        return new PostBuilder(baseUrl);
    }
    /**추후 추가예정**/
    static DeleteBuilder delete(String baseUrl){
        return new DeleteBuilder();
    }
    String getBaseUrl();
    String getPath();
    HttpMethod getMethod();
    HttpHeaders getHeaders();
    class GetBuilder {
        private final String baseUrl;
        private String path = "";
        private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        private HttpHeaders headers = new HttpHeaders();

        public GetBuilder(String baseUrl) { this.baseUrl = baseUrl; }
        public GetBuilder path(String path) { this.path = path; return this; }
        public GetBuilder queryParam(String key, String value) {
            this.queryParams.add(key, value);
            return this;
        }
        public GetBuilder headers(HttpHeaders headers) {
            this.headers=headers;
            return this;
        }
        public GetApiRequest build() {
            return new GetApiRequest(baseUrl, path, queryParams, headers);
        }
    }

    class PostBuilder {
        private final String baseUrl;
        private String path = "";
        private MultiValueMap<String, String> queryParams;
        private Object body;
        private HttpHeaders headers = new HttpHeaders();

        public PostBuilder(String baseUrl) { this.baseUrl = baseUrl; }
        public PostBuilder path(String path) { this.path = path; return this; }

        public PostBuilder body(Object body) { this.body = body; return this; }
        public PostBuilder headers(HttpHeaders headers) {
            this.headers=headers;
            return this;
        }
        public PostApiRequest build() {
            return new PostApiRequest(baseUrl, path, queryParams,body, headers);
        }
    }
    class DeleteBuilder{

    }
}