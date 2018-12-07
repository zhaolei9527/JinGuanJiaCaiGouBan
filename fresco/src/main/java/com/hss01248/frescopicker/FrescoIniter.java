package com.hss01248.frescopicker;

import android.content.Context;

import com.hss01248.frescoloader.FrescoLoader;
import com.hss01248.image.ImageLoader;

import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.utils.Initer;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class FrescoIniter extends Initer {
    @Override
    public void init(Context context) {
        super.init(context);
        ImageLoader.init(context,150,new FrescoLoader());
        PhotoPickUtils.holderGenerator = new FrescoHolderGenerater();
    }
}
