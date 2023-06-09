/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common.net;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class NetBaseTests {
  @Test
  public void interrupt() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch finished = new CountDownLatch(1);
    Thread t = new Thread(() -> {
      started.countDown();
      NetBase.standardBlockerWait(latch);
      finished.countDown();
    });
    t.start();
    started.await();
    t.interrupt();
    finished.await();
  }
}
