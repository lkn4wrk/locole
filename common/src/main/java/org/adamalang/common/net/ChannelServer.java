/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.adamalang.common.gossip.Engine;

/** the server side of the connection */
public class ChannelServer extends ChannelCommon {
  private final SocketChannelSet set;
  private final Handler handler;
  private final int id;

  public ChannelServer(SocketChannel socket, SocketChannelSet set, Handler handler, Engine gossipEngine) {
    super(2, gossipEngine);
    this.set = set;
    this.handler = handler;
    this.id = set.add(socket);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    set.remove(this.id);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf inBuffer = (ByteBuf) msg;
    byte type = inBuffer.readByte();
    int id = inBuffer.readIntLE();
    if (type == 0x10 || type == 0x11) {
      ByteStream upstream = new Remote(streams, id, ctx, () -> flushFromWithinContextExecutor(ctx));
      if (type == 0x11) {
        streams.put(id, gossipEngine.server(upstream));
      } else {
        streams.put(id, handler.create(upstream));
      }
      ByteBuf buffer = Unpooled.buffer();
      buffer.writeByte(0x10);
      buffer.writeIntLE(id);
      ctx.write(buffer);
      flushFromWithinContextExecutor(ctx);
    } else {
      routeCommon(type, id, inBuffer, ctx);
    }
    inBuffer.release();
  }
}
