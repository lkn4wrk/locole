/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.exceptions;

import org.adamalang.runtime.async.AsyncTask;

/** when we abort, we need to restart the loop. A retry indicates what do the document's state */
public class RetryProgressException extends Exception {
  public final AsyncTask failedTask;

  public RetryProgressException(final AsyncTask failedTask) {
    this.failedTask = failedTask;
  }
}
