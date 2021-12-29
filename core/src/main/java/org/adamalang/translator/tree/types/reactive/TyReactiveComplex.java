/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.translator.tree.types.reactive;

import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.expressions.constants.ComplexConstant;

import org.adamalang.translator.tree.types.TySimpleReactive;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.TyNativeComplex;

public class TyReactiveComplex extends TySimpleReactive  {
    public TyReactiveComplex(final Token token) {
        super(token, "RxComplex");
    }

    @Override
    public String getAdamaType() {
        return "complex";
    }

    @Override
    public Expression inventDefaultValueExpression(final DocumentPosition forWhatExpression) {
        return new ComplexConstant(0.0, 0.0, Token.WRAP("0.0")).withPosition(forWhatExpression);
    }

    @Override
    public TyType makeCopyWithNewPosition(final DocumentPosition position, final TypeBehavior newBehavior) {
        return new TyReactiveComplex(token).withPosition(position);
    }

    @Override
    public TyType typeAfterGet(final Environment environment) {
        return new TyNativeComplex(TypeBehavior.ReadOnlyNativeValue, null, token);
    }

    @Override
    public void writeTypeReflectionJson(JsonStreamWriter writer) {
        writer.beginObject();
        writer.writeObjectFieldIntro("nature");
        writer.writeString("reactive_value");
        writer.writeObjectFieldIntro("type");
        writer.writeString("complex");
        writer.endObject();
    }
}