package com.easyshu.shuhelper.utils;

import android.content.Context;

import com.android.annotations.NonNull;
import com.easyshu.shuhelper.data.source.DataRepo;

/**
 * Created by shiyan on 2016/11/23.
 */

public class Injection {

    //此处使用application context防止内存泄漏
    public static DataRepo provideDataRepo(@NonNull Context context){
        return DataRepo.getInstance(context);
    }

}
