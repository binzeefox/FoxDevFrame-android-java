package com.binzee.foxdevframe;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Locale;


/**
 * App部分配置
 *
 * @author tong.xw
 * 2021/01/18 11:54
 */
public final class FoxConfigs {
    private static final String CONFIG_FILE_NAME = "FOX_CONFIG";
    private static final String KEY_LANGUAGE_TAG = "KEY_LANGUAGE_TAG";

    FoxConfigs() {

    }

    /**
     * 读取语言Tag
     */
    public String readLanguageTag() {
        return getSharedPreference()
                .getString(KEY_LANGUAGE_TAG, Locale.getDefault().toLanguageTag());
    }

    /**
     * 写入语言Tag
     */
    public void writeLanguageTag(@NonNull String languageTag) {
        getSharedPreference().edit().putString(KEY_LANGUAGE_TAG, languageTag).apply();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    private SharedPreferences getSharedPreference() {
        return FoxCore.getApplicationContext()
                .getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }
}
