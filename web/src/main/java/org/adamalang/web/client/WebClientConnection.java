/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.web.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.adamalang.web.contracts.WebJsonStream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** a single WebSocket connection */
public class WebClientConnection {
  private final ChannelHandlerContext ctx;
  private final AtomicInteger idgen;
  private final ConcurrentHashMap<Integer, WebJsonStream> streams;
  private final Runnable close;

  WebClientConnection(final ChannelHandlerContext ctx, ConcurrentHashMap<Integer, WebJsonStream> streams, Runnable close) {
    this.ctx = ctx;
    this.idgen = new AtomicInteger(0);
    this.streams = streams;
    this.close = close;
  }

  public int execute(ObjectNode request, WebJsonStream streamback) {
    int id = idgen.incrementAndGet();
    request.put("id", id);
    streams.put(id, streamback);
    ctx.writeAndFlush(new TextWebSocketFrame(request.toString()));
    return id;
  }

  public void close() {
    this.close.run();
  }
}
