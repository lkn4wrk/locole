/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.caravan.data;

import org.adamalang.caravan.index.Region;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class SequenceStorageTests {

  @Test
  public void flow() throws Exception {
    File fileToUse1 = File.createTempFile("adama_", "storage1");
    File fileToUse2 = File.createTempFile("adama_", "storage2");
    MemoryMappedFileStorage storage1 = new MemoryMappedFileStorage(fileToUse1, 512);
    MemoryMappedFileStorage storage2 = new MemoryMappedFileStorage(fileToUse2, 8196);
    SequenceStorage storage = new SequenceStorage(storage1, storage2);
    Assert.assertEquals(8196 + 512, storage.size());
    {
      storage.write(new Region(8, 2), "Hi".getBytes(StandardCharsets.UTF_8));
      byte[] read = storage.read(new Region(8, 2));
      Assert.assertEquals("Hi", new String(read, StandardCharsets.UTF_8));
    }
    {
      storage.write(new Region(1024, 2), "Hi".getBytes(StandardCharsets.UTF_8));
      byte[] read = storage.read(new Region(1024, 2));
      Assert.assertEquals("Hi", new String(read, StandardCharsets.UTF_8));
    }
    Assert.assertNull(storage.read(new Region(100000, 2)));
    storage.flush();
    storage.close();
    fileToUse1.delete();
    fileToUse2.delete();
  }
}
