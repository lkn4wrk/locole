/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.net.client.mocks;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleMockCallback implements Callback<Void> {
  private boolean success;
  private int count;
  private int reason;
  private CountDownLatch latch;

  public SimpleMockCallback() {
    this.latch = new CountDownLatch(1);
    this.success = false;
    this.count = 0;
    this.reason = 0;
  }

  @Override
  public synchronized void success(Void value) {
    count++;
    success = true;
    latch.countDown();
  }

  @Override
  public synchronized void failure(ErrorCodeException ex) {
    count++;
    success = false;
    reason = ex.code;
    latch.countDown();
  }

  public void assertSuccess() throws Exception {
    Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS));
    synchronized (this) {
      Assert.assertEquals(1, count);
      Assert.assertTrue(success);
    }
  }

  public void assertFailure(int code) throws Exception {
    Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS));
    synchronized (this) {
      Assert.assertEquals(1, count);
      Assert.assertFalse(success);
      Assert.assertEquals(code, this.reason);
    }
  }
}
