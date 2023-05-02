/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.rxhtml;

public class TemplateLookupTransformTests extends BaseRxHtmlTest {
  @Override
  public String issues() {
    StringBuilder issues = new StringBuilder();
    issues.append("");
    return issues.toString();
  }
  @Override
  public String gold() {
    StringBuilder gold = new StringBuilder();
    gold.append("JavaScript:(function($){");
    gold.append("\n  $.PG(['fixed',''], function(b,a) {");
    gold.append("\n    b.append($.LT($.pR($.pD(a)),'title',function(x) { return ('' + x).trim(); }));");
    gold.append("\n    b.append($.LT(a,'person',function(x) { return x.agent; }));");
    gold.append("\n    b.append($.LT(a,'person',function(x) { return x.authority; }));");
    gold.append("\n    b.append($.LT(a,'person',function(x) { return ('' + x).toUpperCase(); }));");
    gold.append("\n    b.append($.LT(a,'person',function(x) { return ('' + x).toLowerCase(); }));");
    gold.append("\n    b.append($.LT(a,'person',function(x) { return x; }));");
    gold.append("\n  });");
    gold.append("\n})(RxHTML);");
    gold.append("\nStyle:");
    gold.append("\nShell:<!DOCTYPE html>");
    gold.append("\n<html>");
    gold.append("\n<head><script src=\"https://aws-us-east-2.adama-platform.com/libadama.js\"></script><link rel=\"stylesheet\" href=\"/template.css\"><script src=\"/template.js\"></script></head><body></body><script>RxHTML.init();</script></html>");
    return gold.toString();
  }
  @Override
  public String source() {
    StringBuilder source = new StringBuilder();
    source.append("<forest>");
    source.append("\n    <page uri=\"/\">");
    source.append("\n        <lookup path=\"data:/title\" transform=\"trim\" />");
    source.append("\n        <lookup path=\"person\" transform=\"principal.agent\" />");
    source.append("\n        <lookup path=\"person\" transform=\"principal.authority\" />");
    source.append("\n        <lookup path=\"person\" transform=\"upper\" />");
    source.append("\n        <lookup path=\"person\" transform=\"lower\" />");
    source.append("\n        <lookup path=\"person\" transform=\"nope\" />");
    source.append("\n    </page>");
    source.append("\n</forest>");
    return source.toString();
  }
}
