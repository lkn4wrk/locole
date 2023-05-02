/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.json;

import org.adamalang.runtime.contracts.Perspective;
import org.adamalang.runtime.natives.NtPrincipal;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PrivateViewTests {
  @Test
  public void killing() {
    ArrayList<String> list = new ArrayList<>();
    PrivateView pv =
        new PrivateView(
            NtPrincipal.NO_ONE,
            new Perspective() {
              @Override
              public void data(String data) {
                list.add(data);
              }

              @Override
              public void disconnect() {}
            }, null) {

          @Override
          public long memory() {
            return 0;
          }

          @Override
          public void ingest(JsonStreamReader reader) {}

          @Override
          public void dumpViewer(JsonStreamWriter writer) {}

          @Override
          public void update(JsonStreamWriter writer) {}
        };
    Assert.assertTrue(pv.isAlive());
    pv.kill();
    Assert.assertFalse(pv.isAlive());
    pv.deliver("{}");
    Assert.assertEquals("{}", list.get(0));
  }

  @Test
  public void usurp() {
    ArrayList<String> list = new ArrayList<>();
    PrivateView pv1 =
        new PrivateView(
            NtPrincipal.NO_ONE,
            new Perspective() {
              @Override
              public void data(String data) {
                list.add(data);
              }

              @Override
              public void disconnect() {}
            }, null) {

          @Override
          public long memory() {
            return 0;
          }

          @Override
          public void ingest(JsonStreamReader reader) {}

          @Override
          public void dumpViewer(JsonStreamWriter writer) {}

          @Override
          public void update(JsonStreamWriter writer) {}
        };

    PrivateView pv2 =
        new PrivateView(NtPrincipal.NO_ONE, pv1.perspective, null) {

          @Override
          public long memory() {
            return 0;
          }

          @Override
          public void ingest(JsonStreamReader reader) {}

          @Override
          public void dumpViewer(JsonStreamWriter writer) {}

          @Override
          public void update(JsonStreamWriter writer) {}
        };

    pv2.usurp(pv1);
    Assert.assertTrue(pv1.isAlive());
    pv2.kill();
    Assert.assertFalse(pv1.isAlive());
  }

  @Test
  public void futures() {
    ArrayList<String> list = new ArrayList<>();
    PrivateView pv =
        new PrivateView(
            NtPrincipal.NO_ONE,
            new Perspective() {
              @Override
              public void data(String data) {
                list.add(data);
              }

              @Override
              public void disconnect() {}
            }, null) {

          @Override
          public long memory() {
            return 0;
          }

          @Override
          public void ingest(JsonStreamReader reader) {}

          @Override
          public void dumpViewer(JsonStreamWriter writer) {}

          @Override
          public void update(JsonStreamWriter writer) {}
        };
    Assert.assertFalse(pv.futures("\"outstanding\":[],\"blockers\":[]"));
    Assert.assertTrue(pv.futures("XYZ"));
    Assert.assertFalse(pv.futures("XYZ"));
  }
}
