package com.renjinzl.zltool.view.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

public class ZLUriUtils {

    /**
     * 文件路径转Uri
     *
     * @param path 文件路径
     * @return {@link Uri}
     */
    @Nullable
    public static Uri path2Uri(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        final File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return Uri.fromFile(file);
    }

    /**
     * 资源ID转成Uri
     *
     * @param context    {@link Context}
     * @param resourceId 资源ID
     * @return {@link Uri}
     */
    public static Uri resource2Uri(@NonNull Context context, int resourceId) {
        String uriString = String.format("android.resource://%s/%s",
                context.getPackageName(), String.valueOf(resourceId));
        return Uri.parse(uriString);
    }

    /**
     * 将文件的 Uri 转换成 path
     *
     * @param context Context
     * @param uri     {@link Uri}
     */
    @Nullable
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String uri2Path(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (null == contentResolver) {
            return null;
        }

        final String mScheme = uri.getScheme();
        // 4.4及之后的 是以 content:// 开头的，
        // 示例1：content://com.android.providers.media.documents/document/image%3A235700
        // 示例2：content://com.android.providers.downloads.documents/document/5
        // 示例3：content://media/external/images/media/8302
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(contentResolver, contentUri, null, null);

            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {

                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(contentResolver, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_FILE.equals(mScheme)) {
            // start with file://
            return uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(mScheme)) {
            // start with content:// 示例：content://media/external/images/media/8302
            return getDataColumn(contentResolver, uri, null, null);
        }
        return null;
    }

    private static String getDataColumn(@NonNull ContentResolver resolver, @NonNull Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {MediaStore.Images.Media.DATA};
        try {
            cursor = resolver.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isExternalStorageDocument(@NonNull Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(@NonNull Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(@NonNull Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}