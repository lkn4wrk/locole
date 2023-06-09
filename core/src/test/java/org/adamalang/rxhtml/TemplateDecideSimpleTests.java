/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.rxhtml;

public class TemplateDecideSimpleTests extends BaseRxHtmlTest {
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
    gold.append("\n    var c=$.RX([]);");
    gold.append("\n    c.name='default';");
    gold.append("\n    c.space='space';");
    gold.append("\n    c.key='key';");
    gold.append("\n    c.identity=true;");
    gold.append("\n    $.CONNECT(a,c,'/sign-in');");
    gold.append("\n    c.__();");
    gold.append("\n    var d=$.RX([]);");
    gold.append("\n    d.name='default';");
    gold.append("\n    $.P(b,a,d,function(b,e) {");
    gold.append("\n      var f = $.E('div');");
    gold.append("\n      $.DE(f,e,e,'channel','id','id',true,false,function(h,g) {");
    gold.append("\n        h.append($.T(' Time to decide! '));");
    gold.append("\n      },function(h,g) {");
    gold.append("\n        var i = $.E('span');");
    gold.append("\n        i.append($.T(' Can\\'t decide... yet '));");
    gold.append("\n        h.append(i);");
    gold.append("\n      });");
    gold.append("\n      b.append(f);");
    gold.append("\n    },function(b,e) {");
    gold.append("\n    });");
    gold.append("\n    d.__();");
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
    source.append("\n<page uri=\"/\">");
    source.append("\n        <connection space=\"space\" key=\"key\">");
    source.append("\n            <div rx:if=\"decide:channel\">");
    source.append("\n                Time to decide!");
    source.append("\n                <span rx:else>");
    source.append("\n                    Can't decide... yet");
    source.append("\n                </span>");
    source.append("\n            </div>");
    source.append("\n        </connection>");
    source.append("\n    </page>");
    source.append("\n</forest>");
    return source.toString();
  }
}
