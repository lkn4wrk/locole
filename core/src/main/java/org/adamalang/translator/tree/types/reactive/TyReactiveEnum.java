/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree.types.reactive;

import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.expressions.constants.EnumConstant;
import org.adamalang.translator.tree.types.TySimpleReactive;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.TyNativeEnum;
import org.adamalang.translator.tree.types.shared.EnumStorage;
import org.adamalang.translator.tree.types.traits.IsEnum;
import org.adamalang.translator.tree.types.traits.IsOrderable;

public class TyReactiveEnum extends TySimpleReactive implements //
    IsOrderable, //
    IsEnum //
{
  public final String name;
  public final EnumStorage storage;

  public TyReactiveEnum(final Token nameToken, final EnumStorage storage) {
    super(nameToken, "RxEnumInt32");
    name = nameToken.text;
    this.storage = storage;
  }

  @Override
  public String getAdamaType() {
    return "r<" + name + ">";
  }

  @Override
  public TyType makeCopyWithNewPositionInternal(final DocumentPosition position, final TypeBehavior newBehavior) {
    return new TyReactiveEnum(token, storage).withPosition(position);
  }

  @Override
  public void writeTypeReflectionJson(JsonStreamWriter writer) {
    writer.beginObject();
    writer.writeObjectFieldIntro("nature");
    writer.writeString("reactive_enum");
    writeAnnotations(writer);
    writer.writeObjectFieldIntro("type");
    writer.writeString("enum");
    writer.writeObjectFieldIntro("enum");
    writer.writeString(name);
    writer.writeObjectFieldIntro("options");
    storage.writeTypeReflectionJson(writer);
    writer.endObject();
  }

  @Override
  public Expression inventDefaultValueExpression(final DocumentPosition forWhatExpression) {
    return new EnumConstant(Token.WRAP(name), Token.WRAP("::"), Token.WRAP(storage.getDefaultLabel())).withPosition(forWhatExpression);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public EnumStorage storage() {
    return storage;
  }

  @Override
  public TyType typeAfterGet(final Environment environment) {
    return new TyNativeEnum(TypeBehavior.ReadOnlyNativeValue, token, token, token, storage, token).withPosition(this);
  }
}
