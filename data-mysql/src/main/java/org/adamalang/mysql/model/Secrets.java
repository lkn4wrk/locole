/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.mysql.model;

import org.adamalang.mysql.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Secrets {
  /** insert a secret key */
  public static int insertSecretKey(DataBase dataBase, String space, String privateKeyEncrypted) throws Exception {
    try (Connection connection = dataBase.pool.getConnection()) {
      String sql = new StringBuilder().append("INSERT INTO `").append(dataBase.databaseName).append("`.`secrets` (`space`, `encrypted_private_key`) VALUES (?,?)").toString();
      try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, space);
        statement.setString(2, privateKeyEncrypted);
        statement.execute();
        return DataBase.getInsertId(statement);
      }
    }
  }

  /** get the private key by an id */
  public static String getPrivateKey(DataBase dataBase, String space, int keyId) throws Exception{
    try (Connection connection = dataBase.pool.getConnection()) {
      String sql = new StringBuilder().append("SELECT `encrypted_private_key` FROM `").append(dataBase.databaseName).append("`.`secrets` WHERE `id`=? AND `space`=?").toString();
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, keyId);
        statement.setString(2, space);
        try (ResultSet rs = statement.executeQuery()) {
          if (rs.next()) {
            return rs.getString(1);
          }
        }
      }
    }
    return null;
  }
}