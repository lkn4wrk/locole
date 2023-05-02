/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.delta;

import org.adamalang.runtime.delta.secure.TestKey;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.json.PrivateLazyDeltaWriter;
import org.adamalang.runtime.natives.NtComplex;
import org.adamalang.runtime.natives.NtPrincipal;
import org.junit.Assert;
import org.junit.Test;

public class DComplexTests {
  @Test
  public void flow() {
    final var db = new DComplex();
    final var stream = new JsonStreamWriter();
    final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
    db.show(new NtComplex(1, 2), writer);
    db.show(new NtComplex(3, 4), writer);
    db.show(new NtComplex(3, 4), writer);
    db.show(new NtComplex(3, 4), writer);
    db.hide(writer);
    db.hide(writer);
    db.show(new NtComplex(1, 2), writer);
    db.show(new NtComplex(1, 2), writer);
    Assert.assertEquals("{\"r\":1.0,\"i\":2.0}{\"r\":3.0,\"i\":4.0}null{\"r\":1.0,\"i\":2.0}", stream.toString());
    Assert.assertEquals(48, db.__memory());
    db.clear();
  }
}
