package org.adamalang.runtime.data;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;

import java.util.concurrent.ConcurrentHashMap;

/** a cache a slow finder */
public class CachedFinderProxy implements FinderService {
  private final FinderService finder;
  private final ConcurrentHashMap<Key, Result> cache;

  public CachedFinderProxy(FinderService finder) {
    this.finder = finder;
    this.cache = new ConcurrentHashMap<>();
  }

  @Override
  public void create(Key key, Callback<Void> callback) {
    finder.create(key, callback);
  }

  @Override
  public void find(Key key, Callback<Result> callback) {
    Result cached = cache.get(key);
    if (cached != null) {
      callback.success(cached);
      return;
    }
    finder.find(key, new Callback<Result>() {
      @Override
      public void success(Result value) {
        cache.put(key, value);
        callback.success(value);
      }

      @Override
      public void failure(ErrorCodeException ex) {
        callback.failure(ex);
      }
    });
  }

  /** nuke the cache for the given key */
  public void nuke(Key key) {
    cache.remove(key);
  }

  @Override
  public void takeover(Key key, Callback<Void> callback) {
    finder.takeover(key, callback);
  }

  @Override
  public void archive(Key key, String archiveKey, Callback<Void> callback) {
    finder.archive(key, archiveKey, callback);
  }

  @Override
  public void update(Key key, long deltaSize, long assetSize, Callback<Void> callback) {
    finder.update(key, deltaSize, assetSize, callback);
  }
}
