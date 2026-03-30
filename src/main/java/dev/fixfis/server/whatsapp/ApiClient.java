package dev.fixfis.server.whatsapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private String url;

    public ApiClient(String url) {
        this.url = url;
    }

    public static class ApiCalls<T> {

        private final String endpoint;
        private String token;
        private HttpURLConnection postConn;

        private Map<String, String> headers = new HashMap<>();

        public ApiCalls<T> headers(String key, String value) {
            headers.put(key, value);
            return this;
        }

        private ApiCalls(String endpoint) {
            this.endpoint = endpoint;
        }

        public ApiCalls<T> token(String token) {

            this.token = token;
            return this;
        }

        public ApiCalls<T> post(T body) {
            return request(body, "post");
        }

        public ApiCalls<T> put(T body) {
            return request(body, "put");
        }

        public ApiCalls<T> delete() {
            return request(null, "delete");
        }

        public JsonObject getJson(ResponseHandler<JsonObject> handler) {
            if (postConn == null) {
                return get(JsonObject.class, handler);
            }

            JsonObject result = new Gson().fromJson(gettingString(postConn), JsonObject.class);

            if (handler != null) {
                handler.handle(result);
            }

            return result;
        }

        public JsonObject getJson() {
            return getJson(null);
        }

        public JsonArray getArray(ResponseHandler<JsonArray> handler) {
            if (postConn == null) {
                return get(JsonArray.class, handler);
            }

            JsonArray result = new Gson().fromJson(gettingString(postConn), JsonArray.class);

            if (handler != null) {
                handler.handle(result);
            }

            return result;
        }

        public JsonArray getArray() {
            return getArray(null);
        }

        public <S> List<S> getList(Class<S> sClass, ResponseHandler<List<S>> handler) {
            Type type = TypeToken.getParameterized(List.class, sClass).getType();

            List<S> result = new Gson().fromJson(getArray(), type);

            if (handler != null) {
                handler.handle(result);
            }

            return result;
        }

        public <S> List<S> getList(Class<S> sClass) {
            return getList(sClass, null);
        }

        public <S> S get(Class<S> sClass, ResponseHandler<S> handler) {
            if (postConn == null) {
                return request(null, "get").get(sClass, handler);
            }

            S result = new Gson().fromJson(gettingString(postConn), sClass);

            if (handler != null) {
                handler.handle(result);
            }

            return result;
        }

        public <S> S get(Class<S> sClass) {
            return get(sClass, null);
        }

        private ApiCalls<T> request(T body, String method) {
            try {

                String o;

                if (body == null) o = "";
                else {
                    o = new Gson().toJson(body);
                    System.out.println(o);
                }

                URL url = new URI(endpoint).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(method.toUpperCase());
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                headers.forEach(conn::setRequestProperty);

                conn.setConnectTimeout(10000); // 10s
                conn.setReadTimeout(10000);

                if (token != null) conn.setRequestProperty("Authorization", "Bearer " + token);

                if (!method.equalsIgnoreCase("get")) {
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(o.getBytes());
                    os.flush();
                    os.close();

                }


                postConn = conn;
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @FunctionalInterface
        public interface ResponseHandler<T> {
            void handle(T response);
        }
    }

    public static <T> ApiCalls<T> createFromScratch(String endpoint) {
        return new ApiCalls<>(endpoint);
    }

    public <T> ApiCalls<T> create(String endpoint) {
        return createFromScratch(url + endpoint);
    }


    private static String gettingString(HttpURLConnection conn) {
        try {
            int code = conn.getResponseCode();
            InputStreamReader isr;
            if (code >= 200 && code < 400) {
                isr = new InputStreamReader(conn.getInputStream());
            } else {
                InputStream es = conn.getErrorStream();
                if (es == null) {
                    return "{\"error\":10,\"message\":\"http error: " + code + " " + conn.getURL() + "\"}";
                }
                isr = new InputStreamReader(es);
            }

            BufferedReader reader = new BufferedReader(isr);
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            return "{\"error\": 10,\"message\": \"" + e.getMessage() + "\"}";
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }
}