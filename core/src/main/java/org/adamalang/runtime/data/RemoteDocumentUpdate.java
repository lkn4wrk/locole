/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.runtime.data;

import org.adamalang.runtime.natives.NtClient;

/** the remote copy should change */
public class RemoteDocumentUpdate {
  /** the request that is changing the document */
  public final String request;

  /** the request as as redo patch */
  public final String redo;

  /** the undo patch to revert this change */
  public final String undo;

  /** the beginning sequencer of this change */
  public final int seqBegin;

  /** the end sequencer of this change */
  public final int seqEnd;


  /** who was responsible for the update */
  public final NtClient who;

  /**
   * this update is incomplete with respect to time, and this will ensure we schedule an
   * invalidation in the future
   */
  public final boolean requiresFutureInvalidation;

  /**
   * if requiresFutureInvalidation, then how many milliseconds should the system wait to invoke
   * invalidation
   */
  public final int whenToInvalidateMilliseconds;

  /**
   * how many bytes were written with this update
   */
  public final long assetBytes;

  /** what is the type of the update */
  public final UpdateType updateType;

  public RemoteDocumentUpdate(final int seqBegin, final int seqEnd, NtClient who, final String request, final String redo, final String undo, final boolean requiresFutureInvalidation, int whenToInvalidateMilliseconds, long assetBytes, UpdateType updateType) {
    this.seqBegin = seqBegin;
    this.seqEnd = seqEnd;
    this.who = who;
    this.request = request;
    this.redo = redo;
    this.undo = undo;
    this.requiresFutureInvalidation = requiresFutureInvalidation;
    this.whenToInvalidateMilliseconds = whenToInvalidateMilliseconds;
    this.assetBytes = assetBytes;
    this.updateType = updateType;
  }
}