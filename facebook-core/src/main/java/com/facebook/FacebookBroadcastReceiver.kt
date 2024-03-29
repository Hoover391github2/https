/*
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.facebook.internal.NativeProtocol
import com.facebook.internal.NativeProtocol.isErrorResult

/**
 * This class implements a simple BroadcastReceiver designed to listen for broadcast notifications
 * from the Facebook app. At present, these notifications consistent of success/failure
 * notifications for photo upload operations that happen in the background.
 *
 * Applications may subclass this class and register it in their AndroidManifest.xml. The receiver
 * is listening the com.facebook.platform.AppCallResultBroadcast action.
 */
open class FacebookBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val appCallId = intent.getStringExtra(NativeProtocol.EXTRA_PROTOCOL_CALL_ID)
    val action = intent.getStringExtra(NativeProtocol.EXTRA_PROTOCOL_ACTION)
    val extras = intent.extras
    if (appCallId != null && action != null && extras != null) {
      if (isErrorResult(intent)) {
        onFailedAppCall(appCallId, action, extras)
      } else {
        onSuccessfulAppCall(appCallId, action, extras)
      }
    }
  }

  /**
   * Invoked when the operation was completed successfully.
   *
   * @param appCallId The App Call ID.
   * @param action The action performed.
   * @param extras Any extra information.
   */
  protected open fun onSuccessfulAppCall(appCallId: String, action: String, extras: Bundle) {
    // Default does nothing.
  }

  /**
   * Invoked when the operation failed to complete.
   *
   * @param appCallId The App Call ID.
   * @param action The action performed.
   * @param extras Any extra information.
   */
  protected open fun onFailedAppCall(appCallId: String, action: String, extras: Bundle) {
    // Default does nothing.
  }
}
