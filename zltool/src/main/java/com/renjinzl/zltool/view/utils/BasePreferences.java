package com.renjinzl.zltool.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * @author Created by AdminFun
 * @version v1.0.0
 * @date 2019/5/10 16:31
 * 描述: 轻量级存储工具基类，需实现子类后再使用
 * <p>
 * MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
 * MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件.
 * MODE_WORLD_READABLE 和 MODE_WORLD_WRITEABLE 用来控制其他应用是否有权限读写该文件.
 * MODE_WORLD_READABLE：表示当前文件可以被其他应用读取.
 * MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入.
 */
public abstract class BasePreferences {

    protected SharedPreferences preferences;

    public BasePreferences(@NonNull Context context, @NonNull String name, int mode) {
        preferences = context.getSharedPreferences(name, mode);
        if (null == preferences) {
            preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    /**
     * 存储Integer类型数据
     *
     * @param key   key
     * @param value value
     */
    public void putIntDate(@NonNull String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * 获取Integer数据
     *
     * @param key key
     * @return {@link Integer}
     */
    public int getIntDate(@NonNull String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * 获取Integer数据
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return {@link Integer}
     */
    public int getIntDate(@NonNull String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    /**
     * 存储字符串类型数据
     *
     * @param key   key
     * @param value 值
     */
    public void putStringDate(@NonNull String key, @NonNull String value) {
        preferences.edit().putString(key, value).apply();
    }

    /**
     * 获取字符串类型数据
     *
     * @param key key
     * @return 字符串值，默认为空
     */
    public String getStringDate(@NonNull String key) {
        return this.getStringDate(key, "");
    }

    /**
     * 获取字符串类型数据
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return 字符串值
     */
    public String getStringDate(@NonNull String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /**
     * 储存Long类型数据
     *
     * @param key   key
     * @param value 值
     */
    public void putLongDate(@NonNull String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * 获取Long数据
     *
     * @param key key
     * @return {@link Long}
     */
    public long getLongDate(@NonNull String key) {
        return this.getLongDate(key, 0);
    }

    /**
     * 获取Long数据
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return {@link Long}
     */
    public long getLongDate(@NonNull String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    /**
     * 存储布尔类型数据（自动存入反向布尔值，这取决于之前的布尔值）
     *
     * @param key key
     */
    public void putBoolearnAuto(@NonNull String key) {
        boolean tempValue = this.getBooleanDate(key);
        this.putBooleanDate(key, !tempValue);
    }

    /**
     * 存储布尔类型数据
     *
     * @param key   key
     * @param value 值
     */
    public void putBooleanDate(@NonNull String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取布尔类型数据
     *
     * @param key key
     * @return {@link Boolean}
     */
    public boolean getBooleanDate(@NonNull String key) {
        return this.getBooleanDate(key, false);
    }

    /**
     * 获取布尔类型数据
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return {@link Boolean}
     */
    public boolean getBooleanDate(@NonNull String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * 清除数据
     * @param key
     */
    public void clearDateKey(@NonNull String key){
        preferences.edit().remove(key).apply();
    }
}