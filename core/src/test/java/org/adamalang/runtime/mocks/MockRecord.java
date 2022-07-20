/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.mocks;

import org.adamalang.runtime.contracts.RxParent;
import org.adamalang.runtime.index.ReactiveIndexInvalidator;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.reactives.RxInt32;
import org.adamalang.runtime.reactives.RxRecordBase;
import org.adamalang.runtime.reactives.RxString;
import org.adamalang.runtime.reactives.RxTable;

public class MockRecord extends RxRecordBase<MockRecord> {
  public final RxString data;
  public final RxInt32 index;
  public final ReactiveIndexInvalidator<MockRecord> inv;
  public int id;

  @SuppressWarnings("unchecked")
  public MockRecord(final RxParent p) {
    super(p);
    id = 0;
    data = new RxString(this, "");
    index = new RxInt32(this, 0);
    if (p instanceof RxTable) {
      final var table = (RxTable<MockRecord>) p;
      inv =
          new ReactiveIndexInvalidator<>(table.getIndex((short) 0), this) {
            @Override
            public int pullValue() {
              return index.get();
            }
          };
    } else {
      inv = null;
    }
  }

  public static MockRecord make(final int id) {
    final var mr = new MockRecord(null);
    mr.id = id;
    return mr;
  }

  @Override
  public void __killFields() {

  }

  @Override
  public void __commit(
      final String name, final JsonStreamWriter writer, final JsonStreamWriter reverse) {
    if (__isDirty()) {
      writer.writeObjectFieldIntro(name);
      writer.beginObject();
      reverse.writeObjectFieldIntro(name);
      reverse.beginObject();
      data.__commit("data", writer, reverse);
      index.__commit("index", writer, reverse);
      writer.endObject();
      reverse.endObject();
      __lowerDirtyCommit();
    }
  }

  @Override
  public void __dump(final JsonStreamWriter writer) {
    writer.beginObject();
    writer.writeObjectFieldIntro("data");
    data.__dump(writer);
    writer.writeObjectFieldIntro("index");
    index.__dump(writer);
    writer.endObject();
  }

  @Override
  public void __insert(final JsonStreamReader reader) {
    if (reader.startObject()) {
      while (reader.notEndOfObject()) {
        final var f = reader.fieldName();
        switch (f) {
          case "id":
            id = reader.readInteger();
            break;
          case "data":
            data.__insert(reader);
            break;
          case "index":
            index.__insert(reader);
            break;
          default:
            reader.skipValue();
        }
      }
    }
  }

  @Override
  public void __patch(JsonStreamReader reader) {
    __insert(reader);
  }

  @Override
  public void __revert() {
    if (__isDirty()) {
      __isDying = false;
      data.__revert();
      index.__revert();
      __lowerDirtyRevert();
    }
  }

  @Override
  public void __deindex() {
    if (inv != null) {
      inv.deindex();
    }
  }

  @Override
  public String[] __getIndexColumns() {
    return new String[] {"index"};
  }

  @Override
  public int[] __getIndexValues() {
    return new int[] {index.get()};
  }

  @Override
  public String __name() {
    return null;
  }

  @Override
  public void __reindex() {
    if (inv != null) {
      inv.reindex();
    }
  }

  @Override
  public void __setId(final int __id, final boolean __useForce) {
    id = __id;
  }

  @Override
  public int __id() {
    return id;
  }
}
