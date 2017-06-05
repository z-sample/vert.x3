package org.example.ext;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zero
 *         Created on 2017/6/5.
 */
public class HttpContextImpl implements RoutingContext{

    private RoutingContext delegate;

    public HttpContextImpl(RoutingContext delegate) {
        this.delegate = delegate;
    }

    //////////////////////////////ext/////////////////////////////////////////////

    public void render(String name) {
        delegate.response().end("Mock template : " + name);
    }

    ///////////////////////////////////////////////////////////////////////////

    @CacheReturn
    public HttpServerRequest request() {
        return delegate.request();
    }

    public JsonObject getBodyAsJson() {
        return delegate.getBodyAsJson();
    }

    public JsonArray getBodyAsJsonArray() {
        return delegate.getBodyAsJsonArray();
    }

    public Session session() {
        return delegate.session();
    }

    public boolean removeBodyEndHandler(int handlerID) {
        return delegate.removeBodyEndHandler(handlerID);
    }

    public void setBody(Buffer body) {
        delegate.setBody(body);
    }

    public <T> T remove(String key) {
        return delegate.remove(key);
    }

    public User user() {
        return delegate.user();
    }

    public void setUser(User user) {
        delegate.setUser(user);
    }

    @CacheReturn
    public ParsedHeaderValues parsedHeaders() {
        return delegate.parsedHeaders();
    }

    public <T> T get(String key) {
        return delegate.get(key);
    }

    public boolean removeHeadersEndHandler(int handlerID) {
        return delegate.removeHeadersEndHandler(handlerID);
    }

    public Cookie getCookie(String name) {
        return delegate.getCookie(name);
    }

    public void next() {
        delegate.next();
    }

    public int cookieCount() {
        return delegate.cookieCount();
    }

    public void setAcceptableContentType(String contentType) {
        delegate.setAcceptableContentType(contentType);
    }

    public void fail(int statusCode) {
        delegate.fail(statusCode);
    }

    public Buffer getBody() {
        return delegate.getBody();
    }

    public void clearUser() {
        delegate.clearUser();
    }

    public void reroute(String path) {
        delegate.reroute(path);
    }

    @Deprecated
    @CacheReturn
    public List<Locale> acceptableLocales() {
        return delegate.acceptableLocales();
    }

    @CacheReturn
    public HttpServerResponse response() {
        return delegate.response();
    }

    @CacheReturn
    public Throwable failure() {
        return delegate.failure();
    }

    @GenIgnore
    public Map<String, Object> data() {
        return delegate.data();
    }

    public Route currentRoute() {
        return delegate.currentRoute();
    }

    public Set<FileUpload> fileUploads() {
        return delegate.fileUploads();
    }

    @Fluent
    public RoutingContext addCookie(Cookie cookie) {
        return delegate.addCookie(cookie);
    }

    public boolean failed() {
        return delegate.failed();
    }

    @CacheReturn
    @Deprecated
    public Locale preferredLocale() {
        return delegate.preferredLocale();
    }

    public String mountPoint() {
        return delegate.mountPoint();
    }

    public Map<String, String> pathParams() {
        return delegate.pathParams();
    }

    @CacheReturn
    public List<LanguageHeader> acceptableLanguages() {
        return delegate.acceptableLanguages();
    }

    public void reroute(HttpMethod method, String path) {
        delegate.reroute(method, path);
    }

    public String getAcceptableContentType() {
        return delegate.getAcceptableContentType();
    }

    public Set<Cookie> cookies() {
        return delegate.cookies();
    }

    @CacheReturn
    public int statusCode() {
        return delegate.statusCode();
    }

    public int addBodyEndHandler(Handler<Void> handler) {
        return delegate.addBodyEndHandler(handler);
    }

    public Cookie removeCookie(String name) {
        return delegate.removeCookie(name);
    }

    public String getBodyAsString() {
        return delegate.getBodyAsString();
    }

    public String pathParam(String name) {
        return delegate.pathParam(name);
    }

    public int addHeadersEndHandler(Handler<Void> handler) {
        return delegate.addHeadersEndHandler(handler);
    }

    @CacheReturn
    public LanguageHeader preferredLanguage() {
        return delegate.preferredLanguage();
    }

    @CacheReturn
    public Vertx vertx() {
        return delegate.vertx();
    }

    public void fail(Throwable throwable) {
        delegate.fail(throwable);
    }

    public void setSession(Session session) {
        delegate.setSession(session);
    }

    @Fluent
    public RoutingContext put(String key, Object obj) {
        return delegate.put(key, obj);
    }

    public String normalisedPath() {
        return delegate.normalisedPath();
    }

    public String getBodyAsString(String encoding) {
        return delegate.getBodyAsString(encoding);
    }
}
