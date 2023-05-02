/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.codegen;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.tree.common.StringBuilderWithTabs;
import org.adamalang.translator.tree.expressions.Expression;

import java.util.Map;

/** generate the config for the document factory */
public class CodeGenConfig {
  public static void writeConfig(final StringBuilderWithTabs sb, final Environment environment) {
    // join the disconnected handlers into one
    sb.append("public static HashMap<String, Object> __config() {").tabUp().writeNewline();
    sb.append("HashMap<String, Object> __map = new HashMap<>();").writeNewline();
    for (Map.Entry<String, Expression> entry : environment.document.configs.entrySet()) {
      sb.append("__map.put(\"").append(entry.getKey()).append("\", ");
      entry.getValue().writeJava(sb, environment);
      sb.append(");").writeNewline();
    }
    sb.append("return __map;").tabDown().writeNewline();
    sb.append("}").writeNewline();
  }
}
