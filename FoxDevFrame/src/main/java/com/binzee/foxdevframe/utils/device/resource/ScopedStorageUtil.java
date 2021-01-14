package com.binzee.foxdevframe.utils.device.resource;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.binzee.foxdevframe.FoxCore;
import com.binzee.foxdevframe.ui.tools.requests.ActivityRequester;
import com.binzee.foxdevframe.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


/**
 * 分区存储工具类
 *
 * @author 狐彻
 * 2020/09/08 13:29
 */
@RequiresApi(Build.VERSION_CODES.Q)
public class ScopedStorageUtil {
    private static final String TAG = "ScopedStorageUtil";
    private static final String MIME_TYPE = "mime_type";
    private static final String DISPLAY_NAME = "_display_name";
    private static final String RELATIVE_PATH = "relative_path";

    /* 常用插入Uri */
    public static final Uri URI_IMAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final Uri URI_AUDIO = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final Uri URI_VIDEO = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    public static final Uri URI_DOWNLOAD = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
    public static final Uri URI_DOCUMENTS = MediaStore.Files.getContentUri("external");
    /* 常用插入Uri Finish*/


    private static ScopedStorageUtil sInstance;
    private final ContentResolver mResolver;  //

    /**
     * 私有化构造器
     *
     * @author 狐彻 2020/09/08 13:44
     */
    private ScopedStorageUtil() {
        mResolver = FoxCore.getApplication().getContentResolver();
    }

    /**
     * 双重锁单例
     *
     * @author 狐彻 2020/09/08 13:46
     */
    public static ScopedStorageUtil get() {
        if (sInstance == null) {
            synchronized (ScopedStorageUtil.class) {
                if (sInstance == null) {
                    sInstance = new ScopedStorageUtil();
                }
                return sInstance;
            }
        } else {
            return sInstance;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 执行保存操作
     *
     * @param tableUri 所存表的Uri
     * @author 狐彻 2020/09/08 14:10
     */
    public Writer writer(Uri tableUri) {
        return new Writer(tableUri);
    }

    /**
     * 执行读取操作
     *
     * @param tableUri 所取表的Uri
     * @author 狐彻 2020/09/08 14:34
     */
    public Reader reader(Uri tableUri) {
        return new Reader(tableUri);
    }

    /**
     * 执行删除操作
     *
     * @author 狐彻 2020/09/10 9:35
     */
    public void delete(Uri fileUri) {
        FoxCore.getApplication().getContentResolver()
                .delete(fileUri, null, null);
    }

    /**
     * 开启 文件选择器并返回Future
     *
     * @author tong.xw 2020/12/31 16:28
     */
    public Future<Uri> openFilePicker(@NonNull FragmentManager manager) {
        return ThreadUtils.get().callIO(new Callable<Uri>() {
            final Object lock = new Object();
            volatile Uri result = null;
            volatile boolean finished;

            @Override
            public Uri call() throws Exception {
                ThreadUtils.runOnUiThread(this::requestData);
                if (!finished) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                return result;
            }

            private void requestData() {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                ActivityRequester.get(manager)
                        .request(intent, 0x00f0f, (requestCode, resultCode, resultData) -> {
                            synchronized (lock) {
                                if (resultCode == Activity.RESULT_OK && resultData != null) {
                                    result = resultData.getData();
                                }
                                finished = true;
                                lock.notify();
                            }
                        });
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    public static String getImageFolder(@NonNull String folderName) {
        return Environment.DIRECTORY_PICTURES + "/" + folderName;
    }

    public static String getVideoFolder(@NonNull String folderName) {
        return Environment.DIRECTORY_MOVIES + "/" + folderName;
    }

    public static String getMusicFolder(@NonNull String folderName) {
        return Environment.DIRECTORY_MUSIC + "/" + folderName;
    }

    public static String getDCIMFolder(@NonNull String folderName) {
        return Environment.DIRECTORY_DCIM + "/" + folderName;
    }

    public static String getDownloadFolder(@NonNull String folderName) {
        return Environment.DIRECTORY_DOWNLOADS + "/" + folderName;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 保存者
     *
     * @author 狐彻 2020/09/08 13:48
     */
    public class Writer {
        private final ContentValues values;
        private final Uri uri;

        private Writer(Uri tableUri) {
            values = new ContentValues();
            uri = tableUri;
        }

        /**
         * 设置mime type
         *
         * @author 狐彻 2020/09/08 13:51
         */
        public Writer type(@NonNull String type) {
            values.put(MIME_TYPE, type);
            return this;
        }

        /**
         * 设置文件名（包含尾缀）
         *
         * @author 狐彻 2020/09/08 13:52
         */
        public Writer fileName(@NonNull String fileName) {
            values.put(DISPLAY_NAME, fileName);
            return this;
        }

        /**
         * 设置保存路径
         *
         * @author 狐彻 2020/09/08 13:53
         */
        public Writer path(@NonNull String path) {
            values.put(RELATIVE_PATH, path);
            return this;
        }

        /**
         * 保存
         *
         * @author 狐彻 2020/09/08 15:21
         */
        public Uri save() {
            return mResolver.insert(uri, values);
        }
    }

    /**
     * 读取者
     *
     * @author 狐彻 2020/09/08 14:11
     */
    public class Reader {
        private final String colID = "_id";
        private final Uri uri;

        private Reader(Uri tableUri) {
            uri = tableUri;
        }

        /**
         * 读取该表全部内容
         *
         * @author 狐彻 2020/09/08 14:46
         */
        public List<Uri> read() {
            Cursor cursor = mResolver.query(uri, new String[]{colID, DISPLAY_NAME}
                    , null, null, DISPLAY_NAME);
            if (cursor == null || !cursor.moveToFirst())
                return new ArrayList<>();
            else return getListFromCursor(uri, cursor);
        }

        /**
         * 返回该表内该文件夹所有内容
         *
         * @param folderPath 文件夹路径
         * @author 狐彻 2020/09/08 14:47
         */
        public List<Uri> read(final String folderPath) {
            Cursor cursor = mResolver.query(uri, new String[]{colID, DISPLAY_NAME, RELATIVE_PATH}
                    , RELATIVE_PATH + " = ?", new String[]{folderPath}, DISPLAY_NAME);
            if (cursor == null || !cursor.moveToFirst())
                return new ArrayList<>();
            else return getListFromCursor(uri, cursor);
        }

        /**
         * 从游标中获取列表
         *
         * @author 狐彻 2020/09/08 14:30
         */
        private List<Uri> getListFromCursor(Uri uri, @NonNull Cursor cursor) {
            List<Uri> uriList = new ArrayList<>();
            do {
                int index = cursor.getColumnIndex(colID);
                uriList.add(ContentUris.withAppendedId(uri, index));
            } while (cursor.moveToNext());
            return uriList;
        }

        /**
         * 直接返回游标
         *
         * @author 狐彻 2020/09/08 14:51
         */
        private Cursor getCursor(String[] projection, String selection
                , String[] selectionArgs, String order) {

            return mResolver.query(uri, projection, selection, selectionArgs, order);
        }
    }

    /**
     * 插入结果
     *
     * @author 狐彻 2020/09/08 14:41
     */
    public static class InsertResult {
        private final boolean success;
        private final Uri uri;

        public InsertResult(boolean success, Uri uri) {
            this.success = success;
            this.uri = uri;
        }

        public boolean isSuccess() {
            return success;
        }

        public Uri getUri() {
            return uri;
        }
    }
}