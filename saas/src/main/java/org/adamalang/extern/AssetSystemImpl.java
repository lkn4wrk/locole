/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.extern;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.connection.Session;
import org.adamalang.extern.aws.S3;
import org.adamalang.multiregion.MultiRegionClient;
import org.adamalang.mysql.DataBase;
import org.adamalang.net.client.contracts.SimpleEvents;
import org.adamalang.runtime.contracts.AdamaStream;
import org.adamalang.runtime.data.Key;
import org.adamalang.runtime.natives.NtAsset;
import org.adamalang.transforms.PerSessionAuthenticator;
import org.adamalang.transforms.results.AuthenticatedUser;
import org.adamalang.web.assets.AssetRequest;
import org.adamalang.web.assets.AssetStream;
import org.adamalang.web.assets.AssetSystem;
import org.adamalang.web.assets.AssetUploadBody;
import org.adamalang.web.io.ConnectionContext;

import java.util.concurrent.atomic.AtomicBoolean;

/** a concrete implementation of the asset system */
public class AssetSystemImpl implements AssetSystem {
  public final DataBase database;
  public final MultiRegionClient adama;
  public final S3 s3;

  public AssetSystemImpl(DataBase database, MultiRegionClient adama, S3 s3) {
    this.database = database;
    this.adama = adama;
    this.s3 = s3;
  }

  @Override
  public void request(AssetRequest request, AssetStream stream) {
    s3.request(request, stream);
  }

  @Override
  public void request(Key key, NtAsset asset, AssetStream stream) {
    AssetRequest request = new AssetRequest(key.space, key.key, asset.id);
    s3.request(request, stream);
  }

  @Override
  public void attach(String identity, ConnectionContext context, Key key, NtAsset asset, Callback<Integer> callback) {
    PerSessionAuthenticator authenticator = new PerSessionAuthenticator(database, context, new String[] {});
    authenticator.execute(new Session(authenticator), identity, new Callback<AuthenticatedUser>() {
      @Override
      public void success(AuthenticatedUser who) {
        AtomicBoolean responded = new AtomicBoolean(false);
        AdamaStream stream = adama.connect(who, key.space, key.key, "{}", new SimpleEvents() {
          @Override
          public void connected() {
            /* don't care */
          }

          @Override
          public void delta(String data) { /* don't care */ }

          @Override
          public void error(int code) {
            if (responded.compareAndSet(false, true)) {
              callback.failure(new ErrorCodeException(code));
            }
          }

          @Override
          public void disconnected() {
            if (responded.compareAndSet(false, true)) {
              callback.failure(new ErrorCodeException(-123));
            }
          }
        });
        stream.canAttach(new Callback<>() {
          @Override
          public void success(Boolean value) {
            if (value) {
              stream.attach(asset.id, asset.name, asset.contentType, asset.size, asset.md5, asset.sha384, new Callback<Integer>() {
                @Override
                public void success(Integer value) {
                  if (responded.compareAndSet(false, true)) {
                    callback.success(value);
                    stream.close();
                  }
                }

                @Override
                public void failure(ErrorCodeException ex) {
                  if (responded.compareAndSet(false, true)) {
                    callback.failure(ex);
                    stream.close();
                  }
                }
              });
            } else {
              if (responded.compareAndSet(false, true)) {
                callback.failure(new ErrorCodeException(-123));
                stream.close();
              }
            }
          }

          @Override
          public void failure(ErrorCodeException ex) {
            if (responded.compareAndSet(false, true)) {
              callback.failure(ex);
              stream.close();
            }
          }
        });
      }

      @Override
      public void failure(ErrorCodeException ex) {
        callback.failure(ex);
      }
    });
  }

  @Override
  public void upload(Key key, NtAsset asset, AssetUploadBody body, Callback<Void> callback) {
    s3.upload(key, asset, body, callback);
  }

}
