/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
*/
package org.adamalang.web.io;

import org.adamalang.common.ErrorCodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class JsonResponderHashMapCleanupProxyTests {

    @Test
    public void streamPass() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(42, 1);
        MockJsonResponder responder = new MockJsonResponder();
        JsonResponderHashMapCleanupProxy proxy = new JsonResponderHashMapCleanupProxy((x) -> x.run(), map, 42, responder);
        Assert.assertTrue(map.containsKey(42));
        proxy.stream("X");
        Assert.assertEquals("STREAM:X", responder.events.get(0));
        Assert.assertTrue(map.containsKey(42));
    }

    @Test
    public void finishRemoves() {
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(42L, 1);
        MockJsonResponder responder = new MockJsonResponder();
        JsonResponderHashMapCleanupProxy proxy = new JsonResponderHashMapCleanupProxy((x) -> x.run(), map, 42, responder);
        Assert.assertTrue(map.containsKey(42L));
        proxy.finish("X");
        Assert.assertEquals("FINISH:X", responder.events.get(0));
        Assert.assertFalse(map.containsKey(42L));
    }

    @Test
    public void errorRemoves() {
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(42L, 1);
        MockJsonResponder responder = new MockJsonResponder();
        JsonResponderHashMapCleanupProxy proxy = new JsonResponderHashMapCleanupProxy((x) -> x.run(), map, 42, responder);
        Assert.assertTrue(map.containsKey(42L));
        proxy.error(new ErrorCodeException(123));
        Assert.assertEquals("ERROR:123", responder.events.get(0));
        Assert.assertFalse(map.containsKey(42L));
    }
}