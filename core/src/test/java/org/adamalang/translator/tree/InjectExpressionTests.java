/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.tree.expressions.InjectExpression;
import org.adamalang.translator.tree.types.natives.TyNativeVoid;
import org.junit.Test;

public class InjectExpressionTests {
  @Test
  public void coverage() {
    final InjectExpression ie =
        new InjectExpression(new TyNativeVoid()) {
          @Override
          public void writeJava(final StringBuilder sb, final Environment environment) {}
        };
    ie.typing(null, null);
    ie.emit(null);
    ie.writeJava((StringBuilder) null, null);
  }
}
