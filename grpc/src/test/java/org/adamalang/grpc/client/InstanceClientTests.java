/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.grpc.client;

import org.adamalang.common.metrics.NoOpMetricsFactory;
import org.adamalang.grpc.TestBed;
import org.adamalang.grpc.client.contracts.*;
import org.adamalang.grpc.mocks.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceClientTests {
  @Test
  public void stubPersistent() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10001,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; transition #p in 0.5; } #p { x++; } ")) {
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      try (InstanceClient client =
          new InstanceClient(bed.identity, metrics, null, "127.0.0.1:10001", bed.clientExecutor, lifecycle, new StdErrLogger())) {
        {
          AssertCreateFailure failure = new AssertCreateFailure();
          client.create("nope", "nope", "space", "1", null, "{}", failure);
          Assert.assertFalse(client.ping(2500));
          failure.await(723982);
        }
        bed.startServer();
        Assert.assertTrue(client.ping(15000));
        {
          AssertCreateSuccess success = new AssertCreateSuccess();
          client.create("nope", "nope", "space", "2", "123", "{}", success);
          success.await();
        }
        bed.stopServer();
        Assert.assertFalse(client.ping(15000));
        {
          AssertCreateFailure failure = new AssertCreateFailure();
          client.create("nope", "nope", "space", "3", "42", "{}", failure);
          failure.await(723982);
        }
      }
    }
  }

  @Test
  public void multiplexPersistent() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10002,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; transition #p in 0.1; } #p { x++; } ")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      MockEvents events = new MockEvents();
      Runnable happy = events.latchAt(5);
      Runnable disconnect = events.latchAt(6);
      Runnable reconnect = events.latchAt(8);
      Runnable disconnectAgain = events.latchAt(9);
      AtomicBoolean created = new AtomicBoolean(false);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10002",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  System.err.println("connected");
                  if (created.compareAndExchange(false, true) == false) {
                    AssertCreateSuccess success = new AssertCreateSuccess();
                    client.create("nope", "nope", "space", "1", "123", "{}", success);
                    success.await();
                  }
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        happy.run();
        bed.stopServer();
        disconnect.run();
        bed.startServer();
        reconnect.run();
        bed.stopServer();
        disconnectAgain.run();
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DELTA:{\"data\":{\"x\":124},\"seq\":5}");
        events.assertWrite(3, "DELTA:{\"seq\":6}");
        events.assertWrite(4, "DELTA:{\"seq\":7}");
        events.assertWrite(5, "DISCONNECTED");
        events.assertWrite(6, "CONNECTED");
        events.assertWrite(7, "DELTA:{\"data\":{\"x\":124},\"seq\":12}");
        events.assertWrite(8, "DISCONNECTED");
        Assert.assertEquals("CDCD", lifecycle.toString());
      }
    }
  }

  @Test
  public void sendAndDisconnect() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10003,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      MockEvents events =
          new MockEvents() {
            @Override
            public void connected(Remote remote) {
              remote.send(
                  "foo",
                  null,
                  "{\"z\":\"100\"}",
                  new SeqCallback() {
                    @Override
                    public void success(int seq) {
                      remote.disconnect();
                    }

                    @Override
                    public void error(int code) {
                      System.err.println("error!:" + code);
                    }
                  });
              super.connected(remote);
            }
          };
      Runnable happy = events.latchAt(4);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10003",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DELTA:{\"data\":{\"x\":223},\"seq\":6}");
        events.assertWrite(3, "DISCONNECTED");
      }
    }
  }

  @Test
  public void disconnectThenSendFailure() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10004,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      AtomicInteger errorCodeSeq = new AtomicInteger(0);
      CountDownLatch latch = new CountDownLatch(1);
      MockEvents events =
          new MockEvents() {
            Remote remote = null;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }

            @Override
            public void delta(String data) {
              remote.disconnect();
              super.delta(data);
            }

            @Override
            public void disconnected() {
              remote.send(
                  "foo",
                  "marker1",
                  "{\"z\":\"100\"}",
                  new SeqCallback() {
                    @Override
                    public void success(int seq) {}

                    @Override
                    public void error(int code) {
                      errorCodeSeq.set(code);
                      latch.countDown();
                    }
                  });
              super.disconnected();
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10004",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
        Assert.assertEquals(798735, errorCodeSeq.get());
      }
    }
  }

  @Test
  public void cantAttachPolicy() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10005,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      CountDownLatch cantAttachLatch = new CountDownLatch(1);
      MockEvents events =
          new MockEvents() {
            @Override
            public void connected(Remote remote) {
              remote.canAttach(
                  new AskAttachmentCallback() {
                    @Override
                    public void allow() {}

                    @Override
                    public void reject() {
                      remote.disconnect();
                      cantAttachLatch.countDown();
                    }

                    @Override
                    public void error(int code) {}
                  });
              super.connected(remote);
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10005",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(cantAttachLatch.await(2000, TimeUnit.MILLISECONDS));
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
      }
    }
  }

  @Test
  public void canAttachThenAttach() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10006,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } @can_attach(who) { return true; } @attached (who, what) { x++; } ")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      CountDownLatch canAttachLatch = new CountDownLatch(1);
      MockEvents events =
          new MockEvents() {
            @Override
            public void connected(Remote remote) {
              remote.canAttach(
                  new AskAttachmentCallback() {
                    @Override
                    public void allow() {
                      remote.attach(
                          "id",
                          "name",
                          "text/json",
                          42,
                          "x",
                          "y",
                          new SeqCallback() {
                            @Override
                            public void success(int seq) {
                              remote.disconnect();
                              canAttachLatch.countDown();
                            }

                            @Override
                            public void error(int code) {}
                          });
                    }

                    @Override
                    public void reject() {}

                    @Override
                    public void error(int code) {}
                  });
              super.connected(remote);
            }
          };
      Runnable happy = events.latchAt(4);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10006",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(canAttachLatch.await(2000, TimeUnit.MILLISECONDS));
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DELTA:{\"data\":{\"x\":124},\"seq\":6}");
        events.assertWrite(3, "DISCONNECTED");
      }
    }
  }

  @Test
  public void cantAttachDisconnect() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10007,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      CountDownLatch cantAttachLatch = new CountDownLatch(1);
      AtomicInteger error = new AtomicInteger(0);
      MockEvents events =
          new MockEvents() {
            Remote remote;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }

            @Override
            public void delta(String data) {
              this.remote.disconnect();
              super.delta(data);
            }

            @Override
            public void disconnected() {
              remote.canAttach(
                  new AskAttachmentCallback() {
                    @Override
                    public void allow() {}

                    @Override
                    public void reject() {}

                    @Override
                    public void error(int code) {
                      error.set(code);
                      cantAttachLatch.countDown();
                    }
                  });
              super.disconnected();
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10007",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(cantAttachLatch.await(2000, TimeUnit.MILLISECONDS));
        Assert.assertEquals(798735, error.get());
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
      }
    }
  }

  @Test
  public void cantAttachSocketDisconnect() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10008,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      CountDownLatch cantAttachLatch = new CountDownLatch(1);
      AtomicInteger error = new AtomicInteger(0);
      MockEvents events =
          new MockEvents() {
            Remote remote;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }

            @Override
            public void delta(String data) {
              try {
                bed.stopServer();
              } catch (Exception ex) {

              }
              super.delta(data);
            }

            @Override
            public void disconnected() {
              remote.canAttach(
                  new AskAttachmentCallback() {
                    @Override
                    public void allow() {}

                    @Override
                    public void reject() {}

                    @Override
                    public void error(int code) {
                      error.set(code);
                      cantAttachLatch.countDown();
                    }
                  });
              super.disconnected();
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10008",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(cantAttachLatch.await(2000, TimeUnit.MILLISECONDS));
        Assert.assertEquals(701452, error.get());
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
      }
    }
  }

  @Test
  public void attachSocketDisconnect() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10009,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      CountDownLatch attachLatch = new CountDownLatch(1);
      AtomicInteger error = new AtomicInteger(0);
      MockEvents events =
          new MockEvents() {
            Remote remote;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }

            @Override
            public void delta(String data) {
              try {
                bed.stopServer();
              } catch (Exception ex) {

              }
              super.delta(data);
            }

            @Override
            public void disconnected() {
              remote.attach(
                  "id",
                  "name",
                  "text/json",
                  42,
                  "x",
                  "y",
                  new SeqCallback() {
                    @Override
                    public void success(int seq) {}

                    @Override
                    public void error(int code) {
                      error.set(code);
                      attachLatch.countDown();
                    }
                  });
              super.disconnected();
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10009",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(attachLatch.await(2000, TimeUnit.MILLISECONDS));
        Assert.assertEquals(786442, error.get());
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
      }
    }
  }

  @Test
  public void socketDisconnectThenSendFailure() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10010,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      AtomicInteger errorCodeSeq = new AtomicInteger(0);
      CountDownLatch latch = new CountDownLatch(1);
      MockEvents events =
          new MockEvents() {
            Remote remote = null;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }

            @Override
            public void delta(String data) {
              try {
                bed.stopServer();
              } catch (Exception ex) {

              }
              super.delta(data);
            }

            @Override
            public void disconnected() {
              remote.send(
                  "foo",
                  "marker1",
                  "{\"z\":\"100\"}",
                  new SeqCallback() {
                    @Override
                    public void success(int seq) {}

                    @Override
                    public void error(int code) {
                      errorCodeSeq.set(code);
                      latch.countDown();
                    }
                  });
              super.disconnected();
            }
          };
      Runnable happy = events.latchAt(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10010",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  AssertCreateSuccess success = new AssertCreateSuccess();
                  client.create("nope", "nope", "space", "1", "123", "{}", success);
                  success.await();
                  client.connect("nope", "test", "space", "1", events);
                  lifecycle.connected(client);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        Assert.assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
        events.assertWrite(0, "CONNECTED");
        events.assertWrite(1, "DELTA:{\"data\":{\"x\":123},\"seq\":4}");
        events.assertWrite(2, "DISCONNECTED");
        Assert.assertEquals(777231, errorCodeSeq.get());
      }
    }
  }

  @Test
  public void noSocket() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10011,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      MockClentLifecycle lifecycle = new MockClentLifecycle();
      MockEvents events =
          new MockEvents() {
            Remote remote = null;

            @Override
            public void connected(Remote remote) {
              this.remote = remote;
              super.connected(remote);
            }
          };
      Runnable happy = events.latchAt(1);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10011",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  client.close();
                  client.connect("nope", "test", "space", "1", events);
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {
                  lifecycle.disconnected(client);
                }
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        happy.run();
        events.assertWrite(0, "DISCONNECTED");
      }
    }
  }

  @Test
  public void scanDeploymentsSuccessAndFailures() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
        new TestBed(
            10012,
            "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      CountDownLatch latch = new CountDownLatch(3);
      try (InstanceClient client =
          new InstanceClient(
              bed.identity, metrics, null,
              "127.0.0.1:10012",
              bed.clientExecutor,
              new Lifecycle() {
                @Override
                public void connected(InstanceClient client) {
                  client.scanDeployments("space",
                      new ScanDeploymentCallback() {
                        @Override
                        public void success() {
                          System.err.println("first good");
                          latch.countDown();
                          client.scanDeployments("space",
                              new ScanDeploymentCallback() {
                                @Override
                                public void success() {
                                  System.err.println("second good");
                                  latch.countDown();
                                  client.scanDeployments("space",
                                      new ScanDeploymentCallback() {
                                        @Override
                                        public void success() {}

                                        @Override
                                        public void failure() {
                                          System.err.println("third bad");
                                          latch.countDown();
                                        }
                                      });
                                }

                                @Override
                                public void failure() {}
                              });
                        }

                        @Override
                        public void failure() {}
                      });
                }

                @Override
                public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                @Override
                public void disconnected(InstanceClient client) {}
              },
              (t, errorCode) -> {
                System.err.println("EXCEPTION:" + t.getMessage());
              })) {
        bed.startServer();
        Assert.assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
      }
    }
  }

  @Test
  public void heatMonitoring() throws Exception {
    ClientMetrics metrics = new ClientMetrics(new NoOpMetricsFactory());
    try (TestBed bed =
             new TestBed(
                 10012,
                 "@can_create(who) { return true; } @connected(who) { return true; } public int x; @construct { x = 123; } message Y { int z; } channel foo(Y y) { x += y.z; }")) {
      bed.startServer();
      CountDownLatch latch = new CountDownLatch(3);
      HeatMonitor monitor = new HeatMonitor() {
        @Override
        public void heat(String target, double cpu, double memory) {
          latch.countDown();
        }
      };
      try (InstanceClient client =
               new InstanceClient(
                   bed.identity, metrics, monitor,
                   "127.0.0.1:10012",
                   bed.clientExecutor,
                   new Lifecycle() {
                     @Override
                     public void connected(InstanceClient client) {
                     }

                     @Override
                     public void heartbeat(InstanceClient client, Collection<String> spaces) {}

                     @Override
                     public void disconnected(InstanceClient client) {}
                   },
                   (t, errorCode) -> {
                     System.err.println("EXCEPTION:" + t.getMessage());
                   })) {
        bed.startServer();
        Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS));
        bed.stopServer();
      }
    }
  }
}
