/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.translator.tree.types.checking.ruleset;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.tree.common.DocumentPosition;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.checking.properties.CanMathResult;

public class RuleSetMultiply {
  public static CanMathResult CanMultiply(final Environment environment, final TyType typeA, final TyType typeB, final boolean silent) {
    if (typeA != null && typeB != null) {
      final var aInteger = RuleSetCommon.IsInteger(environment, typeA, true);
      final var bInteger = RuleSetCommon.IsInteger(environment, typeB, true);
      if (aInteger && bInteger) { return CanMathResult.YesAndResultIsInteger; }
      final var aLong = RuleSetCommon.IsLong(environment, typeA, true);
      final var bLong = RuleSetCommon.IsLong(environment, typeB, true);
      if (aLong && bLong || aLong && bInteger || aInteger && bLong) { return CanMathResult.YesAndResultIsLong; }
      final var aString = RuleSetCommon.IsString(environment, typeA, true);
      if (aString && bInteger) { return CanMathResult.YesAndResultIsStringRepetitionUsingSpecialMultiplyOp; }
      final var aDouble = RuleSetCommon.IsDouble(environment, typeA, true);
      final var bDouble = RuleSetCommon.IsDouble(environment, typeB, true);
      if (aDouble && bDouble || aDouble && (bInteger || bLong) || (aInteger || aLong) && bDouble) { return CanMathResult.YesAndResultIsDouble; }
      if (RuleSetLists.TestReactiveList(environment, typeA, true)) {
        final var subTypeA = RuleSetCommon.ExtractEmbeddedType(environment, typeA, silent);
        if (subTypeA != null) {
          final var childToRight = CanMultiply(environment, subTypeA, typeB, silent);
          if (childToRight != CanMathResult.No) { return RuleSetMath.UpgradeToList(childToRight); }
        }
      }
      if (!silent) {
        environment.document.createError(DocumentPosition.sum(typeA, typeB), String.format("Type check failure: the types '%s' and '%s' are unable to be multiplied with the * operator.", typeA.getAdamaType(), typeB.getAdamaType()), "Multiply");
      }
    }
    return CanMathResult.No;
  }
}
