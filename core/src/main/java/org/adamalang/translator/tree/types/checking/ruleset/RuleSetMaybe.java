/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree.types.checking.ruleset;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.natives.TyNativeMaybe;
import org.adamalang.translator.tree.types.reactive.TyReactiveMaybe;
import org.adamalang.translator.tree.types.traits.details.DetailContainsAnEmbeddedType;

public class RuleSetMaybe {
  public static boolean IsMaybe(final Environment environment, final TyType tyTypeOriginal, final boolean silent) {
    final var tyType = RuleSetCommon.Resolve(environment, tyTypeOriginal, silent);
    if (tyType != null) {
      if (tyType instanceof TyNativeMaybe || tyType instanceof TyReactiveMaybe) {
        return true;
      }
      if (!silent) {
        environment.document.createError(tyTypeOriginal, String.format("Type check failure: the type '%s' was expected to be a maybe<?>", tyTypeOriginal.getAdamaType()), "RuleSetMaybe");
      }
    }
    return false;
  }

  public static boolean IsMaybeIntegerOrJustInteger(final Environment environment, final TyType tyTypeOriginal, final boolean silent) {
    final var tyType = RuleSetCommon.Resolve(environment, tyTypeOriginal, silent);
    if (tyType != null) {
      if (tyType instanceof TyNativeMaybe || tyType instanceof TyReactiveMaybe) {
        return RuleSetCommon.IsInteger(environment, ((DetailContainsAnEmbeddedType) tyType).getEmbeddedType(environment), silent);
      }
      return RuleSetCommon.IsInteger(environment, tyType, silent);
    }
    return false;
  }
}
