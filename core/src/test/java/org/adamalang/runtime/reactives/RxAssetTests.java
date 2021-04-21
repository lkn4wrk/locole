package org.adamalang.runtime.reactives;

import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.natives.NtAsset;
import org.junit.Assert;
import org.junit.Test;

public class RxAssetTests {
  private static final NtAsset A = new NtAsset(123, "name", "png", 42, "hash", "sheesh");
  private static final NtAsset B = new NtAsset(42, "name", "jpg", 42, "hash2", "sheesh2");

  @Test
  public void compare() {
    RxAsset a = new RxAsset(null, A);
    RxAsset b = new RxAsset(null, B);
    Assert.assertEquals(1, a.compareTo(b));
    Assert.assertEquals(-1, b.compareTo(a));
  }

  @Test
  public void commit_no_change() {
    JsonStreamWriter redo = new JsonStreamWriter();
    JsonStreamWriter undo = new JsonStreamWriter();

    RxAsset a = new RxAsset(null, A);
    a.__commit("x", redo, undo);
    Assert.assertEquals("", redo.toString());
    Assert.assertEquals("", undo.toString());
  }

  @Test
  public void commit_change() {
    JsonStreamWriter redo = new JsonStreamWriter();
    JsonStreamWriter undo = new JsonStreamWriter();

    RxAsset a = new RxAsset(null, A);
    a.set(B);
    a.__commit("x", redo, undo);
    Assert.assertEquals("\"x\":{\"id\":\"42\",\"size\":\"42\",\"name\":\"name\",\"type\":\"jpg\",\"md5\":\"hash2\",\"sha384\":\"sheesh2\"}", redo.toString());
    Assert.assertEquals("\"x\":{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}", undo.toString());
  }

  @Test
  public void revert() {
    JsonStreamWriter redo = new JsonStreamWriter();
    JsonStreamWriter undo = new JsonStreamWriter();
    RxAsset a = new RxAsset(null, A);
    a.set(B);
    a.__revert();
    a.__commit("x", redo, undo);
    Assert.assertEquals("", redo.toString());
    Assert.assertEquals("", undo.toString());
  }

  @Test
  public void dump() {
    JsonStreamWriter c = new JsonStreamWriter();
    RxAsset a = new RxAsset(null, A);
    a.__dump(c);
    Assert.assertEquals("{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}", c.toString());
  }

  @Test
  public void insert() {
    JsonStreamWriter c = new JsonStreamWriter();
    RxAsset a = new RxAsset(null, B);
    a.__dump(c);
    a.__insert(new JsonStreamReader("{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}"));
    a.__dump(c);
    Assert.assertEquals("{\"id\":\"42\",\"size\":\"42\",\"name\":\"name\",\"type\":\"jpg\",\"md5\":\"hash2\",\"sha384\":\"sheesh2\"}{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}", c.toString());
  }


  @Test
  public void patch() {
    JsonStreamWriter c = new JsonStreamWriter();
    RxAsset a = new RxAsset(null, B);
    a.__patch(new JsonStreamReader("{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}"));
    a.__dump(c);
    Assert.assertEquals("{\"id\":\"123\",\"size\":\"42\",\"name\":\"name\",\"type\":\"png\",\"md5\":\"hash\",\"sha384\":\"sheesh\"}", c.toString());
  }

}
