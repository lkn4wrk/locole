/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.translator.tree;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.definitions.DocumentEvent;
import org.adamalang.translator.tree.definitions.FunctionSpecialization;
import org.adamalang.translator.tree.definitions.MessageHandlerBehavior;
import org.adamalang.translator.tree.expressions.EnvLookupName;
import org.adamalang.translator.tree.expressions.MessageConversionStyle;
import org.adamalang.translator.tree.expressions.constants.DynamicNullConstant;
import org.adamalang.translator.tree.operands.AssignmentOp;
import org.adamalang.translator.tree.operands.BinaryOp;
import org.adamalang.translator.tree.operands.PostfixMutateOp;
import org.adamalang.translator.tree.operands.PrefixMutateOp;
import org.adamalang.translator.tree.statements.ControlFlow;
import org.adamalang.translator.tree.statements.control.AlterControlFlowMode;
import org.adamalang.translator.tree.types.checking.properties.AssignableEmbedType;
import org.adamalang.translator.tree.types.checking.properties.CanAssignResult;
import org.adamalang.translator.tree.types.checking.properties.CanBumpResult;
import org.adamalang.translator.tree.types.checking.properties.CanMathResult;
import org.adamalang.translator.tree.types.checking.properties.CanTestEqualityResult;
import org.adamalang.translator.tree.types.checking.properties.StorageTweak;
import org.adamalang.translator.tree.types.checking.properties.WrapInstruction;
import org.adamalang.translator.tree.types.structures.StorageSpecialization;
import org.adamalang.translator.tree.types.traits.details.IndexLookupStyle;
import org.junit.Assert;
import org.junit.Test;

public class SillyEnumCoverageTests {
  @Test
  public void coverage() {
    Assert.assertNull(BinaryOp.fromText("NOPEx"));
    PostfixMutateOp.fromText("!x");
    PrefixMutateOp.fromText("!x");
    AssignmentOp.fromText("!x");
    WrapInstruction.None.toString();
    WrapInstruction.valueOf("WrapBWithMaybe");
    MessageHandlerBehavior.EnqueueItemIntoNativeChannel.toString();
    MessageHandlerBehavior.valueOf("EnqueueItemIntoNativeChannel");
    DocumentEvent.ClientConnected.toString();
    DocumentEvent.valueOf("ClientConnected");
    FunctionSpecialization.Pure.toString();
    FunctionSpecialization.valueOf("Pure");
    MessageConversionStyle.Maybe.toString();
    MessageConversionStyle.valueOf("Maybe");
    AlterControlFlowMode.Abort.toString();
    AlterControlFlowMode.valueOf("Abort");
    ControlFlow.Open.toString();
    ControlFlow.valueOf("Open");
  }

  @Test
  public void dyn_null() {
    new DynamicNullConstant(Token.WRAP("null")).emit((t) -> {});
  }

  @Test
  public void coverageSimple() {
    AssignableEmbedType.None.toString();
    CanAssignResult.No.toString();
    CanBumpResult.No.toString();
    CanMathResult.No.toString();
    CanTestEqualityResult.No.toString();
    StorageTweak.None.toString();
    WrapInstruction.None.toString();
    StorageSpecialization.Message.toString();
    ComputeContext.Assignment.toString();
    EnvLookupName.Blocked.toString();
    IndexLookupStyle.ExpressionLookupMethod.toString();
    IndexLookupStyle.ExpressionGetOrCreateMethod.toString();
    ComputeContext.Computation.toString();
  }
}
