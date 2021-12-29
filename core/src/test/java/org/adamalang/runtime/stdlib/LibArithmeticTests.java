/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.runtime.stdlib;

import org.adamalang.runtime.natives.NtMaybe;
import org.junit.Assert;
import org.junit.Test;

public class LibArithmeticTests {
    @Test
    public void doubleDivision() {
        Assert.assertEquals(0.5, LibArithmetic.divDD(1, 2.0).get(), 0.01);
        Assert.assertEquals(0.5, LibArithmetic.divDD(new NtMaybe<>(1.0), 2).get(), 0.01);
        Assert.assertEquals(0.5, LibArithmetic.divDD(1.0, new NtMaybe<>(2.0)).get(), 0.01);
        Assert.assertEquals(0.5, LibArithmetic.divDD(new NtMaybe<>(1.0), new NtMaybe<>(2.0)).get(), 0.01);
        Assert.assertFalse(LibArithmetic.divDD(new NtMaybe<>(), 2).has());
        Assert.assertFalse(LibArithmetic.divDD(1.0, new NtMaybe<>()).has());
        Assert.assertFalse(LibArithmetic.divDD(new NtMaybe<>(), new NtMaybe<>()).has());
        Assert.assertFalse(LibArithmetic.divDD(new NtMaybe<>(1.0), new NtMaybe<>()).has());
        Assert.assertFalse(LibArithmetic.divDD(new NtMaybe<>(), new NtMaybe<>(1.0)).has());
        Assert.assertFalse(LibArithmetic.divDD(1, 0.0).has());
    }

    @Test
    public void intDivision() {
        Assert.assertEquals(0.5, LibArithmetic.divII(1, 2).get(), 0.01);
        Assert.assertFalse(LibArithmetic.divII(1, 0).has());
    }
}