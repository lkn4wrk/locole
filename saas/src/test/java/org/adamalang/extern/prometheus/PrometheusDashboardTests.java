/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.extern.prometheus;

import org.junit.Test;

import java.io.File;

public class PrometheusDashboardTests {
  @Test
  public void coverage() throws Exception {
    PrometheusDashboard pd = new PrometheusDashboard();
    pd.page("x", "X");
    pd.section("title");
    pd.counter("c");
    pd.inflight("inf");
    pd.makeCallbackMonitor("cb");
    pd.makeItemActionMonitor("iam");
    pd.page("y", "Y");
    pd.makeRequestResponseMonitor("rrm");
    pd.makeStreamMonitor("sm");
    File parentTemp = File.createTempFile("ADAMATEST_", "tampy").getParentFile();
    File prometh = new File(parentTemp, "promethesus-" + System.currentTimeMillis());
    try {
      prometh.mkdir();
      pd.finish(prometh);
    } finally {
      for (File file : prometh.listFiles()) {
        file.delete();
      }
      prometh.delete();
    }
  }
}
