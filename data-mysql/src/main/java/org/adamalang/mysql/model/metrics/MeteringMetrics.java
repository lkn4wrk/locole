/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.mysql.model.metrics;

import org.adamalang.common.metrics.MetricsFactory;

public class MeteringMetrics {
  public final Runnable metering_batch_found;
  public final Runnable metering_batch_late;
  public final Runnable metering_batch_just_right;
  public final Runnable metering_batch_early;

  public MeteringMetrics(MetricsFactory factory) {
    metering_batch_found = factory.counter("metering_batch_found");
    metering_batch_late = factory.counter("metering_batch_late");
    metering_batch_just_right = factory.counter("metering_batch_just_right");
    metering_batch_early = factory.counter("metering_batch_early");
  }
}
