/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
package org.example.template;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.TemplateEngine;

import java.io.FileNotFoundException;

/**
 * 如果使用此类处理模板, 修改模板后还是需要重新构建甚至重启应用(vert.x默认的方案也一样)
 *
 * @author Zero
 */
class TemplateHandlerImpl implements TemplateHandler {

    private final TemplateEngine engine;
    private final String templateDirectory;
    private final String contentType;
    private final String suffix;

    public TemplateHandlerImpl(TemplateEngine engine, String templateDirectory, String contentType, String suffix) {
        this.engine = engine;
        this.templateDirectory = templateDirectory;
        this.contentType = contentType;
        this.suffix = suffix;
    }

    @Override
    public void handle(RoutingContext context) {
//        String file = templateDirectory + Utils.pathOffset(context.normalisedPath(), context);
        String template = context.get("$");
        StringBuilder builder = new StringBuilder(templateDirectory);
        if (template != null) {
            builder.append("/");
            builder.append(template);
        } else {
            if (context.normalisedPath().endsWith("/")) {
                builder.append(context.normalisedPath().substring(0, context.normalisedPath().length() - 1));
            } else {
                builder.append(context.normalisedPath());
            }
        }
        builder.append(this.suffix);
        String name = builder.toString();
        context.vertx().fileSystem().exists(name, ar -> {
            if (ar.succeeded() && ar.result()) {
                //模板引擎对JsonObject支持不好,转为Map
                context.data().forEach((key, value) -> {
                    if (value instanceof JsonObject) {
                        context.put(key, ((JsonObject) value).getMap());
                    }
                });
                //渲染
                engine.render(context, name, res -> {
                    if (res.succeeded()) {
                        context.put("req", context.request());
                        context.put("session", context.session());
                        context.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType).end(res.result());
                    } else {
                        res.cause().printStackTrace();
                        context.fail(res.cause());
                    }
                });
            } else {
                context.fail(new FileNotFoundException(name));
            }
        });

    }


}
