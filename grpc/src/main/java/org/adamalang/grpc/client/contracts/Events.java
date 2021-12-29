/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.grpc.client.contracts;

import org.adamalang.grpc.client.InstanceClient;

/** event structure that clients will learn about what happens for a connection to a document */
public interface Events {
    /** the connection was successful, and we can talk to the document via the remote */
    public void connected(InstanceClient.Remote remote);

    /** a data change has occurred */
    public void delta(String data);

    /** an error has occurred */
    public void error(int code);

    /** the document was disconnected */
    public void disconnected();
}