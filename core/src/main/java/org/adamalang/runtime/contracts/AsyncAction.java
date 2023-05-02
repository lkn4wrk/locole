/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.contracts;

import org.adamalang.runtime.exceptions.AbortMessageException;

/**
 * this is a lazy way of associating code to run within a queue. It's basically a runnable that can
 * throw an abort
 */
@FunctionalInterface
public interface AsyncAction {
  /** execute the given task, and maybe abort */
  void execute() throws AbortMessageException;
}
