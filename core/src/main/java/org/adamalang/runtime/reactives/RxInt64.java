/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.reactives;

import org.adamalang.runtime.contracts.CanGetAndSet;
import org.adamalang.runtime.contracts.Indexable;
import org.adamalang.runtime.contracts.RxParent;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;

/** a reactive 64-bit integer (long) */
public class RxInt64 extends RxBase implements Comparable<RxInt64>, CanGetAndSet<Long>, Indexable {
  private long backup;
  private long value;

  public RxInt64(final RxParent owner, final long value) {
    super(owner);
    backup = value;
    this.value = value;
  }

  @Override
  public void __commit(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    if (__isDirty()) {
      forwardDelta.writeObjectFieldIntro(name);
      forwardDelta.writeLong(value);
      reverseDelta.writeObjectFieldIntro(name);
      reverseDelta.writeLong(backup);
      backup = value;
      __lowerDirtyCommit();
    }
  }

  @Override
  public void __dump(final JsonStreamWriter writer) {
    writer.writeLong(value);
  }

  @Override
  public void __insert(final JsonStreamReader reader) {
    backup = reader.readLong();
    value = backup;
  }

  @Override
  public void __patch(JsonStreamReader reader) {
    set(reader.readLong());
  }

  @Override
  public void __revert() {
    if (__isDirty()) {
      value = backup;
      __lowerDirtyRevert();
    }
  }

  @Override
  public long __memory() {
    return super.__memory() + 16;
  }

  public long bumpDownPost() {
    final var result = value--;
    __raiseDirty();
    return result;
  }

  public long bumpDownPre() {
    final var result = --value;
    __raiseDirty();
    return result;
  }

  public long bumpUpPost() {
    final var result = value++;
    __raiseDirty();
    return result;
  }

  // these make ZERO sense
  public long bumpUpPre() {
    final var result = ++value;
    __raiseDirty();
    return result;
  }

  @Override
  public int compareTo(final RxInt64 other) {
    return Long.compare(value, other.value);
  }

  @Override
  public Long get() {
    return value;
  }

  @Override
  public void set(final Long value) {
    if (this.value != value) {
      this.value = value;
      __raiseDirty();
    }
  }

  @Override
  public int getIndexValue() {
    return (int) value;
  }

  public long opAddTo(final long incoming) {
    value += incoming;
    __raiseDirty();
    return value;
  }

  public long opMultBy(final long x) {
    value *= x;
    __raiseDirty();
    return value;
  }

  public void set(final int value) {
    if (this.value != value) {
      this.value = value;
      __raiseDirty();
    }
  }
}
