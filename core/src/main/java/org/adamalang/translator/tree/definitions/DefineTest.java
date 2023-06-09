/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree.definitions;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.statements.Block;

import java.util.function.Consumer;

/** defines a test to run on an empty document, this helps validate flow */
public class DefineTest extends Definition {
  public final Block code;
  public final String name;
  public final Token nameToken;
  public final Token testToken;

  public DefineTest(final Token testToken, final Token nameToken, final Block code) {
    this.testToken = testToken;
    this.nameToken = nameToken;
    name = nameToken.text;
    this.code = code;
    ingest(code);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    yielder.accept(testToken);
    yielder.accept(nameToken);
    code.emit(yielder);
  }

  @Override
  public void typing(final Environment environment) {
    code.typing(environment.scopeAsUnitTest());
  }
}
