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

package com.facebook.share.model

import android.os.Parcel
import android.os.Parcelable

/** Describes content (video, photo, sticker) to be shared into story. */
class ShareStoryContent : ShareContent<ShareStoryContent, ShareStoryContent.Builder> {
  /** The background asset of the story, could be a photo or video */
  val backgroundAsset: ShareMedia<*, *>?

  /** The sticker asset of the story, should be a photo */
  val stickerAsset: SharePhoto?

  /** A list of color which will be draw from top to bottom */
  val backgroundColorList: List<String>?
    get() = field?.toList()

  /** The link that set by 3rd party app */
  val attributionLink: String?

  private constructor(builder: Builder) : super(builder) {
    backgroundAsset = builder.backgroundAsset
    stickerAsset = builder.stickerAsset
    backgroundColorList = builder.backgroundColorList
    attributionLink = builder.attributionLink
  }

  internal constructor(parcel: Parcel) : super(parcel) {
    backgroundAsset = parcel.readParcelable(ShareMedia::class.java.classLoader)
    stickerAsset = parcel.readParcelable(SharePhoto::class.java.classLoader)
    backgroundColorList = readUnmodifiableStringList(parcel)
    attributionLink = parcel.readString()
  }

  override fun describeContents(): Int = 0

  override fun writeToParcel(out: Parcel, flags: Int) {
    super.writeToParcel(out, flags)
    out.writeParcelable(backgroundAsset, 0)
    out.writeParcelable(stickerAsset, 0)
    out.writeStringList(backgroundColorList)
    out.writeString(attributionLink)
  }

  private fun readUnmodifiableStringList(parcel: Parcel): List<String>? {
    val list: List<String> = ArrayList()
    parcel.readStringList(list)
    return if (list.isEmpty()) null else list.toList()
  }

  /** Builder for the [ShareStoryContent]. */
  class Builder : ShareContent.Builder<ShareStoryContent, Builder>() {
    internal var backgroundAsset: ShareMedia<*, *>? = null
    internal var stickerAsset: SharePhoto? = null
    internal var backgroundColorList: List<String>? = null
    internal var attributionLink: String? = null

    /**
     * Set the Background Asset to display
     *
     * @param backgroundAsset the background asset of the story, could be a photo or video
     * @return The builder.
     */
    fun setBackgroundAsset(backgroundAsset: ShareMedia<*, *>?): Builder {
      this.backgroundAsset = backgroundAsset
      return this
    }

    /**
     * Set the Sticker Asset to display
     *
     * @param stickerAsset the sticker asset of the story, should be a photo
     * @return The builder.
     */
    fun setStickerAsset(stickerAsset: SharePhoto?): Builder {
      this.stickerAsset = stickerAsset
      return this
    }

    /**
     * Set the background color list to display
     *
     * @param backgroundColorList a list of color which will be draw from top to bottom
     * @return The builder.
     */
    fun setBackgroundColorList(backgroundColorList: List<String>?): Builder {
      this.backgroundColorList = backgroundColorList?.toList()
      return this
    }

    /**
     * Set the attribution link
     *
     * @param attributionLink link that set by 3rd party app
     * @return The builder.
     */
    fun setAttributionLink(attributionLink: String?): Builder {
      this.attributionLink = attributionLink
      return this
    }

    override fun build(): ShareStoryContent {
      return ShareStoryContent(this)
    }

    override fun readFrom(model: ShareStoryContent?): Builder {
      return if (model == null) {
        this
      } else
          super.readFrom(model)
              .setBackgroundAsset(model.backgroundAsset)
              .setStickerAsset(model.stickerAsset)
              .setBackgroundColorList(model.backgroundColorList)
              .setAttributionLink(model.attributionLink)
    }
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ShareStoryContent> =
        object : Parcelable.Creator<ShareStoryContent> {
          override fun createFromParcel(parcel: Parcel): ShareStoryContent {
            return ShareStoryContent(parcel)
          }

          override fun newArray(size: Int): Array<ShareStoryContent?> {
            return arrayOfNulls(size)
          }
        }
  }
}
