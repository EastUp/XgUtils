package com.east.east_utils.utils.media

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

/**
 * |---------------------------------------------------------------------------------------------------------------|
 * @description:  音频播放工具类
 * @author: East
 * @date: 2019-12-06 21:35
 * |---------------------------------------------------------------------------------------------------------------|
 */
class MediaPlayerManager(private val mContext: Context) {
    private var mLeftVolume = 0f
    private var mRightVolume = 0f
    private var mBackgroundMediaPlayer: MediaPlayer? = null
    private var mIsPaused = false
    private var mCurrentPath: String? = null
    //初始化一些数据
    private fun initData() {
        mLeftVolume = 0.6f
        mRightVolume = 0.6f
        mBackgroundMediaPlayer = null
        mIsPaused = false
        mCurrentPath = null
    }

    /**
     * 根据path路径播放背景音乐
     *
     * @param path :assets中的音频路径
     * @param isLoop :是否循环播放
     */
    fun playBackgroundMusic(path: String, isLoop: Boolean) {
        if (mCurrentPath == null) { //这是第一次播放背景音乐--- it is the first time to play background music
//或者是执行end()方法后，重新被叫---or end() was called
            mBackgroundMediaPlayer = createMediaPlayerFromAssets(path)
            mCurrentPath = path
        } else {
            if (mCurrentPath != path) { //播放一个新的背景音乐--- play new background music
//释放旧的资源并生成一个新的----release old resource and create a new one
                if (mBackgroundMediaPlayer != null) {
                    mBackgroundMediaPlayer!!.release()
                }
                mBackgroundMediaPlayer = createMediaPlayerFromAssets(path)
                //记录这个路径---record the path
                mCurrentPath = path
            }
        }
        if (mBackgroundMediaPlayer == null) {
            Log.e(
                TAG,
                "playBackgroundMusic: background media player is null"
            )
        } else { // 若果音乐正在播放或已近中断，停止它---if the music is playing or paused, stop it
            mBackgroundMediaPlayer!!.stop()
            mBackgroundMediaPlayer!!.isLooping = isLoop
            try {
                mBackgroundMediaPlayer!!.prepare()
                mBackgroundMediaPlayer!!.seekTo(0)
                mBackgroundMediaPlayer!!.start()
                mIsPaused = false
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "playBackgroundMusic: error state"
                )
            }
        }
    }

    /**
     * 从 assets 中创建音乐播放器
     */
    private fun createMediaPlayerFromAssets(path: String): MediaPlayer? {
        var mediaPlayer: MediaPlayer?
        try {
            val assetFileDescriptor = mContext.assets.openFd(path)
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset, assetFileDescriptor.length
            )
            mediaPlayer.prepare()
            mediaPlayer.setVolume(mLeftVolume, mRightVolume)
        } catch (e: Exception) {
            mediaPlayer = null
            Log.e(
                TAG,
                "create mediaPlayer error: " + e.message
            )
        }
        return mediaPlayer
    }

    /**
     * 停止播放背景音乐
     */
    fun stopBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer!!.stop()
            // should set the state, if not , the following sequence will be error
// play -> pause -> stop -> resume
            mIsPaused = false
        }
    }

    /**
     * 暂停播放背景音乐
     */
    fun pauseBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && mBackgroundMediaPlayer!!.isPlaying) {
            mBackgroundMediaPlayer!!.pause()
            mIsPaused = true
        }
    }

    /**
     * 继续播放背景音乐
     */
    fun resumeBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && mIsPaused) {
            mBackgroundMediaPlayer!!.start()
            mIsPaused = false
        }
    }

    /**
     * 重新播放背景音乐
     */
    fun rewindBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer!!.stop()
            try {
                mBackgroundMediaPlayer!!.prepare()
                mBackgroundMediaPlayer!!.seekTo(0)
                mBackgroundMediaPlayer!!.start()
                mIsPaused = false
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "rewindBackgroundMusic: error state"
                )
            }
        }
    }

    /**
     * 判断背景音乐是否正在播放
     */
    val isPlaying: Boolean
        get() {
            val ret: Boolean
            ret = if (mBackgroundMediaPlayer == null) {
                false
            } else {
                mBackgroundMediaPlayer!!.isPlaying
            }
            return ret
        }

    /**
     * 结束背景音乐，并释放资源
     */
    fun end() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer!!.release()
        }
        //重新“初始化数据”
        initData()
    }

    /**
     * 得到背景音乐的“音量”
     */
    /**
     * 设置背景音乐的音量
     */
    var backgroundVolume: Float
        get() = if (mBackgroundMediaPlayer != null) {
            (mLeftVolume + mRightVolume) / 2
        } else {
            0.0f
        }
        set(volume) {
            mRightVolume = volume
            mLeftVolume = mRightVolume
            if (mBackgroundMediaPlayer != null) {
                mBackgroundMediaPlayer!!.setVolume(mLeftVolume, mRightVolume)
            }
        }

    companion object {
        private const val TAG = "MediaPlayerManager"
    }

    init {
        initData()
    }
}