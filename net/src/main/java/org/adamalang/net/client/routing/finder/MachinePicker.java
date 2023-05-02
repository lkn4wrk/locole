/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.net.client.routing.finder;

import org.adamalang.common.Callback;
import org.adamalang.runtime.data.Key;

/** pick a machine from the current region */
public interface MachinePicker {

  /** pick a host for the given key */
  public void pickHost(Key key, Callback<String> callback);
}
