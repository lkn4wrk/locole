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
import org.adamalang.runtime.contracts.RxParent;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;

/** a reactive string */
public class RxString extends RxBase implements Comparable<RxString>, CanGetAndSet<String> {
  protected String backup;
  protected String value;

  public RxString(final RxParent owner, final String value) {
    super(owner);
    backup = value;
    this.value = value;
  }

  @Override
  public void __commit(String name, JsonStreamWriter forwardDelta, JsonStreamWriter reverseDelta) {
    if (__isDirty()) {
      forwardDelta.writeObjectFieldIntro(name);
      forwardDelta.writeString(value);
      reverseDelta.writeObjectFieldIntro(name);
      reverseDelta.writeString(backup);
      backup = value;
      __lowerDirtyCommit();
    }
  }

  @Override
  public void __dump(final JsonStreamWriter writer) {
    writer.writeString(value);
  }

  @Override
  public void __insert(final JsonStreamReader reader) {
    backup = reader.readString();
    value = backup;
  }

  @Override
  public void __patch(JsonStreamReader reader) {
    set(reader.readString());
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
    return super.__memory() + (backup.length() + value.length()) * 2L + 16;
  }

  @Override
  public int compareTo(final RxString other) {
    return value.compareTo(other.value);
  }

  @Override
  public String get() {
    return value;
  }

  @Override
  public void set(final String value) {
    this.value = value;
    __raiseDirty();
  }

  public boolean has() {
    return !value.isEmpty();
  }

  public String opAddTo(final Object incoming) {
    value += incoming.toString();
    __raiseDirty();
    return value;
  }
}
