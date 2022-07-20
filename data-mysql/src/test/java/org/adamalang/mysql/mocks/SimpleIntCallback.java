/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.mysql.mocks;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.junit.Assert;

public class SimpleIntCallback implements Callback<Integer> {
  public Integer value;
  private boolean success;
  private int count;
  private int reason;

  public SimpleIntCallback() {
    this.value = null;
    this.success = false;
    this.count = 0;
    this.reason = -1;
  }
  @Override
  public void success(Integer value) {
    this.value = value;
    this.success = true;
    this.count++;
  }

  @Override
  public void failure(ErrorCodeException ex) {
    this.reason = ex.code;
    this.success = false;
    this.count++;
  }

  public void assertSuccess(int value) {
    Assert.assertEquals(1, count);
    Assert.assertTrue(success);
    Assert.assertEquals(value, (int) this.value);
  }

  public void assertFailure(int code) {
    Assert.assertEquals(1, count);
    Assert.assertFalse(success);
    Assert.assertEquals(code, this.reason);
  }
}
