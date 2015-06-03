/*
 * Copyright 2015 Dmitriy Manzhosov, Yevgen Kulik, Dmitriy Kovalenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fullscreen.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * Created by mda on 2/3/15.
 */
public class ImageLoaderUtils {

    public static final int MEMORY_CACHE_LIMIT = 2 * 1024 * 1024;
    public static final int THREAD_POOL_SIZE = 5;
    public static final int MAX_IMAGE_WIDTH_FOR_MEMORY_CACHE = 640;
    public static final int MAX_IMAGE_HEIGHT_FOR_MEMORY_CACHE = 640;

    public static void initImageLoader(ImageLoader imageLoader, Context context) {
        initImageLoader(imageLoader, context, -1);
    }

    public static void initImageLoader(ImageLoader imageLoader, Context context, int showImageId) {

        if (imageLoader.isInited()) {
            imageLoader.destroy();
        }

        DisplayImageOptions options = createDisplayOptions(showImageId);

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(MAX_IMAGE_WIDTH_FOR_MEMORY_CACHE, MAX_IMAGE_HEIGHT_FOR_MEMORY_CACHE)
                .threadPoolSize(THREAD_POOL_SIZE)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_LIMIT))
                .denyCacheImageMultipleSizesInMemory();

        imageLoader.init(builder.build());
    }

    public static DisplayImageOptions createDisplayOptions(int showImageId) {
        DisplayImageOptions.Builder displayBuilder = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500));

        if (showImageId != -1) {

            displayBuilder.showImageForEmptyUri(showImageId)
                    .showImageOnFail(showImageId)
                    .showImageOnLoading(showImageId);
        }

        return displayBuilder.build();
    }

    public static DisplayImageOptions createDisplayOptions() {
        DisplayImageOptions.Builder displayBuilder = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500));
        return displayBuilder.build();
    }

    public static DisplayImageOptions createResizeDisplayOptions(int showImageId) {
        DisplayImageOptions.Builder displayBuilder = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new FadeInBitmapDisplayer(500))
                .showImageForEmptyUri(showImageId)
                .delayBeforeLoading(500)
                .showImageOnFail(showImageId)
                .showImageOnLoading(showImageId)
                .preProcessor(new BitmapProcessor() {

                    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
                        int sourceWidth = source.getWidth();
                        int sourceHeight = source.getHeight();

                        // Compute the scaling factors to fit the new height and width, respectively.
                        // To cover the final image, the final scaling will be the bigger
                        // of these two.
                        float xScale = (float) newWidth / sourceWidth;
                        float yScale = (float) newHeight / sourceHeight;
                        float scale = Math.max(xScale, yScale);

                        // Now get the size of the source bitmap when scaled
                        float scaledWidth = scale * sourceWidth;
                        float scaledHeight = scale * sourceHeight;

                        // Let's find out the upper left coordinates if the scaled bitmap
                        // should be centered in the new size give by the parameters
                        float left = (newWidth - scaledWidth) / 2;
                        float top = (newHeight - scaledHeight) / 2;

                        // The target rectangle for the new, scaled version of the source bitmap will now
                        // be
                        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

                        // Finally, we create a new bitmap of the specified size and draw our new,
                        // scaled bitmap onto it.
                        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(dest);
                        canvas.drawBitmap(source, null, targetRect, null);

                        return dest;
                    }

                    @Override
                    public Bitmap process(Bitmap bm) {
                        return scaleCenterCrop(bm, 200, 200);
                    }
                });

        return displayBuilder.build();
    }
}
