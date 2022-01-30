/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.overlord.roles;

import org.adamalang.common.NamedRunnable;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.grpc.client.Client;
import org.adamalang.mysql.DataBase;
import org.adamalang.mysql.frontend.Billing;
import org.adamalang.mysql.frontend.Metering;
import org.adamalang.mysql.frontend.Spaces;
import org.adamalang.mysql.frontend.data.MeteringSpaceSummary;
import org.adamalang.mysql.frontend.data.ResourcesPerPenny;
import org.adamalang.overlord.OverlordMetrics;
import org.adamalang.overlord.html.ConcurrentCachedHtmlHandler;
import org.adamalang.overlord.html.FixedHtmlStringLoggerTable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.HashMap;

public class HourlyAccountant {

  public static class HourlyAccountantTask extends NamedRunnable {
    private final OverlordMetrics metrics;
    private final SimpleExecutor executor;
    private final DataBase dataBase;
    private int billingHourAt;
    private final FixedHtmlStringLoggerTable accountantTable;
    private final ConcurrentCachedHtmlHandler handler;

    public HourlyAccountantTask(OverlordMetrics metrics, DataBase dataBase, ConcurrentCachedHtmlHandler handler) throws Exception {
      super("hourly-accountant");
      this.metrics = metrics;
      this.executor = SimpleExecutor.create("hourly-accountant-executor");
      this.dataBase = dataBase;
      this.accountantTable = new FixedHtmlStringLoggerTable(128, "action", "notes", "value");
      Integer pickUp = Spaces.getLatestBillingHourCode(dataBase);
      if (pickUp != null && pickUp > 0) {
        this.billingHourAt = pickUp;
        accountantTable.row("found-latest-billing-code", "first try", "" + this.billingHourAt);
      } else {
        Long firstRecordAt = Metering.getEarliestRecordTimeOfCreation(dataBase);
        if (firstRecordAt != null) {
          this.billingHourAt = toHourCode(LocalDateTime.ofInstant(Instant.ofEpochMilli(firstRecordAt), ZoneId.systemDefault()).minusHours(6));
          accountantTable.row("found-latest-billing-code", "from metering", "" + this.billingHourAt);
        } else {
          this.billingHourAt = toHourCode(LocalDateTime.now().minusHours(6));
          accountantTable.row("found-latest-billing-code", "from neither (fresh)", "" + this.billingHourAt);
        }
      }
      this.handler = handler;
      executor.execute(this);
    }

    public void runBilling(int forHour) throws Exception {
      metrics.accountant_task.run();
      LocalDateTime from = fromHourCode(forHour);
      LocalDateTime to = from.plusHours(1);
      long fromMs = from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      long toMs = to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      HashMap<String, MeteringSpaceSummary> summaries = Metering.summarizeWindow(dataBase, fromMs, toMs);
      // TODO: need a more formal rate structure as this is B.S.
      ResourcesPerPenny rates = new ResourcesPerPenny(1000 * 1000, 1000, 50, 1000 * 1000, 200);
      long pennies = Billing.transcribeSummariesAndUpdateBalances(dataBase, forHour, summaries, rates);
      accountantTable.row("transcribe-summary", "pennies:" + pennies, "at:" + forHour);
    }

    @Override
    public void execute() throws Exception {
      try {
        handler.put("/accountant", accountantTable.toHtml("Accountant Work"));
        int current = toHourCode(LocalDateTime.now().minusHours(2));
        while (this.billingHourAt < current) {
          int hourToRun = nextHour(billingHourAt);
          runBilling(hourToRun);
          billingHourAt = hourToRun;
        }
        accountantTable.row("accountant-ran", "hour:" + current, "scan:" + System.currentTimeMillis());
        handler.put("/accountant", accountantTable.toHtml("Accountant Work"));
      } finally {
        executor.schedule(this, 1000 * 60 * 10);
      }
    }
  }

  public static int toHourCode(LocalDateTime localDateTime) {
    return localDateTime.getYear() * 1000000 + localDateTime.getMonth().getValue() * 10000 + localDateTime.getDayOfMonth() * 100 + localDateTime.getHour();
  }

  public static LocalDateTime fromHourCode(int hourCode) {
    int v = hourCode;
    int hour = v % 100;
    v /= 100;
    int day = v % 100;
    v /= 100;
    int month = v % 100;
    v /= 100;
    int year = v;
    return LocalDateTime.of(year, month, day, hour, 0);
  }

  public static int nextHour(int hour) {
    return toHourCode(fromHourCode(hour).plusHours(1));
  }

  public static void kickOff(OverlordMetrics metrics, DataBase dataBaseFront, ConcurrentCachedHtmlHandler handler) throws Exception {
    new HourlyAccountantTask(metrics, dataBaseFront, handler);
  }
}