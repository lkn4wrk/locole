/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.runtime.natives;

import org.junit.Assert;
import org.junit.Test;

public class NtCreateContextTests {
    @Test
    public void coverage() {
        NtCreateContext context = new NtCreateContext("origin", "ip", "key");
        Assert.assertEquals("origin", context.origin);
        Assert.assertEquals("ip", context.ip);
        Assert.assertEquals("key", context.key);
    }
}
