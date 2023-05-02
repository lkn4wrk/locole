/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common;

import java.util.concurrent.ThreadFactory;

/** name a thread on creation; this enables executors to be named in a meaningful way */
public class NamedThreadFactory implements ThreadFactory {

  private final String name;

  public NamedThreadFactory(String name) {
    this.name = name;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r, name);
    thread.setDaemon(true);
    return thread;
  }
}
