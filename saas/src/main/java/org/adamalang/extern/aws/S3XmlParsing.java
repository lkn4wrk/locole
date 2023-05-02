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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class S3XmlParsing {

  public static class ListResult {
    public final String[] keys;
    public final boolean truncated;
    public ListResult(String[] keys, boolean truncated) {
      this.keys = keys;
      this.truncated = truncated;
    }

    public String last() {
      if (keys.length > 0) {
        return keys[keys.length - 1];
      }
      return null;
    }
  }

  public static ListResult listResultOf(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    ArrayList<String> keys = new ArrayList<>();
    NodeList nodeList = doc.getElementsByTagName("Key");
    for (int k = 0; k < nodeList.getLength(); k++) {
      keys.add(nodeList.item(k).getTextContent());
    }
    boolean truncated = "true".equals(doc.getElementsByTagName("IsTruncated").item(0).getTextContent());
    return new ListResult(keys.toArray(new String[keys.size()]), truncated);
  }
}
