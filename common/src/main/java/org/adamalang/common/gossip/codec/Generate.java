/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.common.gossip.codec;

import org.adamalang.common.codec.CodecCodeGen;

import java.io.File;
import java.nio.file.Files;

public class Generate {
  public static void main(String[] args) throws Exception {
    String codec = CodecCodeGen.assembleCodec("org.adamalang.common.gossip.codec", "GossipProtocolCodec", GossipProtocol.class.getDeclaredClasses());
    Files.writeString(new File("./common/src/main/java/org/adamalang/common/gossip/codec/GossipProtocolCodec.java").toPath(), codec);
  }
}
