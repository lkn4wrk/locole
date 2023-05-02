/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree.statements;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.StringBuilderWithTabs;

import java.util.function.Consumer;

/** ; */
public class EmptyStatement extends Statement {
  public final Token emptyStatementToken;

  public EmptyStatement(final Token emptyStatementToken) {
    this.emptyStatementToken = emptyStatementToken;
    ingest(emptyStatementToken);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    yielder.accept(emptyStatementToken);
  }

  @Override
  public ControlFlow typing(final Environment environment) {
    return ControlFlow.Open;
  }

  @Override
  public void writeJava(final StringBuilderWithTabs sb, final Environment environment) {
    sb.append(";").writeNewline();
  }

  @Override
  public void free(FreeEnvironment environment) {
  }
}
