/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.NamedRunnable;
import org.adamalang.connection.Session;
import org.adamalang.validators.ValidateChannel;
import org.adamalang.web.io.*;

/** Send a message to the document on the given channel with a dedupe key such that sending happens at most once. */
public class ConnectionSendOnceRequest {
  public final Long connection;
  public final String channel;
  public final String dedupe;
  public final JsonNode message;

  public ConnectionSendOnceRequest(final Long connection, final String channel, final String dedupe, final JsonNode message) {
    this.connection = connection;
    this.channel = channel;
    this.dedupe = dedupe;
    this.message = message;
  }

  public static void resolve(Session session, ConnectionNexus nexus, JsonRequest request, Callback<ConnectionSendOnceRequest> callback) {
    try {
      final Long connection = request.getLong("connection", true, 405505);
      final String channel = request.getString("channel", true, 454659);
      ValidateChannel.validate(channel);
      final String dedupe = request.getString("dedupe", false, 0);
      final JsonNode message = request.getJsonNode("message", true, 425987);
      nexus.executor.execute(new NamedRunnable("connectionsendonce-success") {
        @Override
        public void execute() throws Exception {
           callback.success(new ConnectionSendOnceRequest(connection, channel, dedupe, message));
        }
      });
    } catch (ErrorCodeException ece) {
      nexus.executor.execute(new NamedRunnable("connectionsendonce-error") {
        @Override
        public void execute() throws Exception {
          callback.failure(ece);
        }
      });
    }
  }

  public void logInto(ObjectNode _node) {
    _node.put("channel", channel);
    _node.put("dedupe", dedupe);
  }
}
