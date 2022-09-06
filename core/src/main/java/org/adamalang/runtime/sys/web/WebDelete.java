/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.sys.web;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.natives.NtDynamic;
import org.adamalang.runtime.natives.NtMap;

import java.util.Map;
import java.util.TreeMap;

/** a web delete */
public class WebDelete {
  public final WebContext context;
  public final String uri;
  public final WebRouter router;
  public final NtMap<String, String> headers;
  public final NtDynamic parameters;

  public WebDelete(WebContext context, String uri, TreeMap<String, String> headers, NtDynamic parameters) {
    this.context = context;
    this.uri = uri;
    this.router = new WebRouter(uri);
    this.headers = new NtMap<>();
    this.headers.storage.putAll(headers);
    this.parameters = parameters;
  }

  public static WebDelete read(WebContext context, JsonStreamReader reader) {
    String uri = null;
    NtDynamic parameters = null;
    TreeMap<String, String> headers = null;

    if (reader.startObject()) {
      while (reader.notEndOfObject()) {
        final var fieldName = reader.fieldName();
        switch (fieldName) {
          case "uri":
            uri = reader.readString();
            break;
          case "headers":
            headers = new TreeMap<>();
            if (reader.startObject()) {
              while (reader.notEndOfObject()) {
                String key = reader.fieldName();
                headers.put(key, reader.readString());
              }
            }
            break;
          case "parameters":
            parameters = reader.readNtDynamic();
            break;
        }
      }
    }

    if (uri != null && headers != null && parameters != null) {
      return new WebDelete(context, uri, headers, parameters);
    }
    return null;
  }

  public void write(JsonStreamWriter writer) {
    writer.writeObjectFieldIntro("delete");
    writer.beginObject();
    writer.writeObjectFieldIntro("uri");
    writer.writeString(uri);
    writer.writeObjectFieldIntro("headers");
    writer.beginObject();
    for (Map.Entry<String, String> entry : headers.entries()) {
      writer.writeObjectFieldIntro(entry.getKey());
      writer.writeString(entry.getValue());
    }
    writer.endObject();
    writer.writeObjectFieldIntro("parameters");
    writer.writeNtDynamic(parameters);
    writer.endObject();
  }
}