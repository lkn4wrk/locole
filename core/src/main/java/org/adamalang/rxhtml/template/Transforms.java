/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.rxhtml.template;

import java.util.Locale;

public class Transforms {
  public static String of(String transform) {
    switch (transform.trim().toLowerCase(Locale.ROOT)) {
      case "principal.agent":
        return "function(x) { return x.agent; }";
      case "principal.authority":
        return "function(x) { return x.authority; }";
      case "trim":
        return "function(x) { return ('' + x).trim(); }";
      case "upper":
        return "function(x) { return ('' + x).toUpperCase(); }";
      case "lower":
        return "function(x) { return ('' + x).toLowerCase(); }";
      default:
        return "function(x) { return x; }";
    }
  }
}
