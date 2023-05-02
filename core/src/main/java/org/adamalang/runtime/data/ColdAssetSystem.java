/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's Apache2); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.data;

import org.adamalang.common.Callback;

import java.util.List;

/** list assets for an object that are stored; this is for garbage collection of assets */
public interface ColdAssetSystem {

  /** list all the asset ids for a given key */
  public void listAssetsOf(Key key, Callback<List<String>> callback);

  /** delete the asset for a given document */
  public void deleteAsset(Key key, String assetId, Callback<Void> callback);
}
