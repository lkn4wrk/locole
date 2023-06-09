/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.rxhtml.acl;

import org.adamalang.rxhtml.acl.commands.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Parser {
  public static ArrayList<Command> parse(String command) {
    ArrayList<Command> commands = new ArrayList<>();
    for (String phrase : command.split(Pattern.quote(" "))) {
      int kColon = phrase.indexOf(':');
      if (kColon > 0) {
        String cmd = phrase.substring(0, kColon);
        String body = phrase.substring(kColon + 1).trim();

        switch (cmd) {
          case "toggle": {
            commands.add(new Toggle(body));
            break;
          }
          case "increment":
          case "inc": {
            commands.add(new Increment(body));
            break;
          }
          case "custom":
            commands.add(new Custom(body));
            break;
          case "decrement":
          case "dec": {
            commands.add(new Decrement(body));
            break;
          }
          case "raise": {
            commands.add(new Raise(body));
            break;
          }
          case "lower": {
            commands.add(new Lower(body));
            break;
          }
          case "decide": {
            String[] parts = body.split(Pattern.quote("|"));
            commands.add(new Decide(parts[0], parts.length > 1 ? parts[1] : "id", parts.length > 2 ? parts[2] : "id"));
            break;
          }
          case "choose": {
            String[] parts = body.split(Pattern.quote("|"));
            commands.add(new Choose(parts[0], parts.length > 1 ? parts[1] : "id", parts.length > 2 ? parts[2] : "id"));
            break;
          }
          case "finalize": {
            commands.add(new Finalize(body));
            break;
          }
          case "set": {
            int kEq = body.indexOf('=');
            if (kEq > 0) {
              commands.add(new Set(body.substring(0, kEq).trim(), body.substring(kEq + 1).trim()));
            }
          }
          case "force-auth": {
            int kEq = body.indexOf('=');
            if (kEq > 0) {
              commands.add(new ForceAuth(body.substring(0, kEq).trim(), body.substring(kEq + 1).trim()));
            }
          }
        }
      } else {

      }
    }
    return commands;
  }
}
