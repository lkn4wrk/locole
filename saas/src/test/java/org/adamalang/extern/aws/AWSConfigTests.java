/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.extern.aws;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.ConfigObject;
import org.adamalang.common.Json;
import org.junit.Assert;
import org.junit.Test;

public class AWSConfigTests {
  @Test
  public void coverage() throws Exception {
    ObjectNode aws = Json.newJsonObject();
    aws.put("access-key", "key");
    aws.put("secret-key", "secret");
    aws.put("init-from-email", "x@x.com");
    aws.put("init-reply-email", "x@x.com");
    aws.put("bucket", "bucket");
    aws.put("region", "region");
    aws.put("archive", "archive");
    aws.put("queue", "queue");
    AWSConfig config = new AWSConfig(new ConfigObject(aws));
    Assert.assertEquals("secret", config.secretKey);
    Assert.assertEquals("key", config.accessKeyId);
  }
}
