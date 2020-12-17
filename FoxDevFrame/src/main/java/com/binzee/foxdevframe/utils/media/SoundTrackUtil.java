package com.binzee.foxdevframe.utils.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.binzee.foxdevframe.FoxCore;

import java.util.List;

/**
 * 声音工具类
 * <p>
 * 用于播放Raw文件内的声音
 *
 * @author tong.xw
 * 2020/12/17 19:07
 */
@SuppressWarnings("UnusedReturnValue")
public class SoundTrackUtil {
    private static final String TAG = "SoundTrackUtil";
    private SoundPool mPool;
    private final AudioManager mManager;

    private final SparseIntArray mIdArray = new SparseIntArray();   //存放流ID
    private int currentStreamId = 0;    //当前播放流

    /**
     * 私有化构造器
     */
    private SoundTrackUtil() {
        mManager = (AudioManager) FoxCore.getApplication().getSystemService(Context.AUDIO_SERVICE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 初始化重载
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化
     *
     * @param pool 提供声音池
     */
    public static void init(@NonNull SoundPool pool) {
        get().mPool = pool;
    }

    /**
     * 初始化
     *
     * @param attributes 参数
     * @param maxStreams 最大音流数
     */
    public static void init(@Nullable AudioAttributes attributes, int maxStreams) {
        SoundPool.Builder builder = new SoundPool.Builder()
                .setMaxStreams(maxStreams);
        if (attributes != null) builder.setAudioAttributes(attributes);
        get().mPool = builder.build();
    }

    /**
     * 初始化
     *
     * @param maxStreams 最大音流数
     */
    public static void init(int maxStreams) {
        init(null, maxStreams);
    }

    /**
     * 初始化
     * <p>
     * 默认单音流
     *
     * @param attributes 参数
     */
    public static void init(@NonNull AudioAttributes attributes) {
        init(attributes, 1);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 静态方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 静态获取
     */
    public static SoundTrackUtil get() {
        return Holder.sInstance;
    }

    /**
     * 静态播放方法，快捷调用，快乐开发
     *
     * @param loadedRawId 注册过的RawId
     * @see SoundTrackUtil#play(int)
     */
    public static void sPlay(@RawRes int loadedRawId) {
        get().play(loadedRawId);
    }

    /**
     * 静态播放方法，快捷调用，快乐开发
     *
     * @param loadedRawId 注册过的RawId
     * @param loop        若0为不循环，-1为无限循环，其它数值为循环次数（播放次数等于循环次数+1）
     * @see SoundTrackUtil#play(int, int)
     */
    public static void sPlay(@RawRes int loadedRawId, int loop) {
        get().play(loadedRawId);
    }

    /**
     * 静态静音方法
     *
     * @see SoundTrackUtil#silence()
     */
    public static void sSilence() {
        get().silence();
    }

    /**
     * 静态暂停方法
     *
     * @see SoundTrackUtil#pause()
     */
    public static void sPause() {
        get().silence();
    }

    /**
     * 静态恢复播放方法
     *
     * @see SoundTrackUtil#resume()
     */
    public static void sResume() {
        get().resume();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 非静态
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 加载声音
     *
     * @param soundId 声音文件的ID
     */
    public SoundTrackUtil loadSound(@RawRes int soundId) {
        mIdArray.append(soundId, getSoundPool().load(FoxCore.getApplication(), soundId, 1));
        return this;
    }

    /**
     * 批量加载声音
     */
    public void loadSounds(@NonNull List<Integer> soundIdList) {
        for (int id : soundIdList)
            loadSound(id);
    }

    /**
     * 播放声音
     *
     * @param soundId 注册过的声音资源文件
     * @param loop    若0为不循环，-1为无限循环，其它数值为循环次数（播放次数等于循环次数+1）
     */
    public void play(@RawRes int soundId, int loop) {
        if (currentStreamId != 0) getSoundPool().stop(currentStreamId);
        float volRatio = getVolumeRatio();
        currentStreamId = getSoundPool().play(mIdArray.get(soundId), volRatio, volRatio, 0, loop, 1);
    }

    /**
     * 播放一次声音
     *
     * @param soundId 注册过的声音ID
     */
    public void play(@RawRes int soundId) {
        play(soundId, 0);
    }

    /**
     * 停止播放声音
     */
    public void silence() {
        if (currentStreamId != 0) getSoundPool().stop(currentStreamId);
        currentStreamId = 0;
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (currentStreamId == 0) return;
        getSoundPool().pause(currentStreamId);
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (currentStreamId == 0) return;
        getSoundPool().resume(currentStreamId);
    }

    /**
     * 当前是否有声音流
     */
    public boolean isInStream() {
        return currentStreamId != 0;
    }

    /**
     * 获取声音管理器
     */
    public AudioManager getAudioManager() {
        return mManager;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取音轨
     */
    private SoundPool getSoundPool() {
        if (mPool == null) throw new UnInitialException();
        return mPool;
    }

    /**
     * 获取音量
     */
    private float getVolumeRatio() {
        // 获取最大音量值
        float audioMaxVolume = mManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        // 不断获取当前的音量值
        float audioCurrentVolume = mManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        //最终影响音量
        return Math.max(audioCurrentVolume / audioMaxVolume, 0.6f);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    private static class UnInitialException extends RuntimeException {
        UnInitialException() {
            super("尚未初始化");
        }
    }

    private static final class Holder {
        private static final SoundTrackUtil sInstance = new SoundTrackUtil();
    }
}
