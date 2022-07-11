/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.translator.tree.definitions;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.expressions.Expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

/** Define a service */
public class DefineService extends Definition {
  public static class ServiceAspect {
    public final Token name;
    public final Token equals;
    public final Expression expression;
    public final Token semicolon;

    public ServiceAspect(Token name, Token equals, Expression expression, Token semicolon) {
      this.name = name;
      this.equals = equals;
      this.expression = expression;
      this.semicolon = semicolon;
    }

    public void emit(Consumer<Token> yielder) {
      yielder.accept(name);
      yielder.accept(equals);
      expression.emit(yielder);
      yielder.accept(semicolon);
    }
  }

  public static class ServiceMethod {
    public final Token methodToken;
    public final Token pairOpen;
    public final Token inputTypeName;
    public final Token comma;
    public final Token outputTypeName;
    public final Token outputArrayExt;
    public final Token pairClose;
    public final Token name;
    public final Token semicolon;

    public ServiceMethod(Token methodToken, Token pairOpen, Token inputTypeName, Token comma, Token outputTypeName, Token outputArrayExt, Token pairClose, Token name, Token semicolon) {
      this.methodToken = methodToken;
      this.pairOpen = pairOpen;
      this.inputTypeName = inputTypeName;
      this.comma = comma;
      this.outputTypeName = outputTypeName;
      this.outputArrayExt = outputArrayExt;
      this.pairClose = pairClose;
      this.name = name;
      this.semicolon = semicolon;
    }

    public void emit(Consumer<Token> yielder) {
      yielder.accept(methodToken);
      yielder.accept(pairOpen);
      yielder.accept(inputTypeName);
      yielder.accept(comma);
      yielder.accept(outputTypeName);
      if (outputArrayExt != null) {
        yielder.accept(outputArrayExt);
      }
      yielder.accept(pairClose);
      yielder.accept(name);
      yielder.accept(semicolon);
    }
  }

  public final Token serviceToken;
  public final Token open;
  public final Token name;
  public final ArrayList<ServiceAspect> aspects;
  public final ArrayList<ServiceMethod> methods;
  public final ArrayList<Consumer<Consumer<Token>>> emission;
  public final Token close;

  public DefineService(Token serviceToken, Token open, Token name, ArrayList<ServiceAspect> aspects, ArrayList<ServiceMethod> methods, Token close, ArrayList<Consumer<Consumer<Token>>> emission) {
    this.serviceToken = serviceToken;
    this.open = open;
    this.name = name;
    this.aspects = aspects;
    this.methods = methods;
    this.emission = emission;
    this.close = close;
  }

  @Override
  public void emit(Consumer<Token> yielder) {
    yielder.accept(serviceToken);
    yielder.accept(open);
    yielder.accept(name);
    for (Consumer<Consumer<Token>> e : emission) {
      e.accept(yielder);
    }
    yielder.accept(close);
  }

  @Override
  public void typing(Environment environment) {
    HashSet<String> alreadyDefinedAspects = new HashSet<>();
    for (ServiceAspect aspect : aspects) {
      if (alreadyDefinedAspects.contains(aspect.name.text)) {
        environment.document.createError(this, String.format("'%s' was already defined as an aspect within the service.", aspect.name.text), "ServiceDefine");
      }
      alreadyDefinedAspects.add(aspect.name.text);
      aspect.expression.typing(environment.scopeWithComputeContext(ComputeContext.Computation), null);
    }
    HashSet<String> alreadyDefinedMethods = new HashSet<>();
    for (ServiceMethod method : methods) {
      if (alreadyDefinedMethods.contains(method.name.text)) {
        environment.document.createError(this, String.format("'%s' was already defined as a method within the service.", method.name.text), "ServiceDefine");
      }
      alreadyDefinedMethods.add(method.name.text);
      environment.rules.FindMessageStructure(method.inputTypeName.text, this, false);
      environment.rules.FindMessageStructure(method.outputTypeName.text, this, false);
    }
  }
}
