package com.east.east_utils.utils.media

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import java.io.IOException

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  声音控制类
 *  @author: East
 *  @date: 2019-12-07 20:18
 * |---------------------------------------------------------------------------------------------------------------|
 */
object MediaHelper {
    private var mPlayer: MediaPlayer? = null
    private var isPause = false
    fun playSound(filePath: String?, listener: OnCompletionListener?) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer()
        } else {
            mPlayer!!.reset()
        }
        mPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mPlayer!!.setOnCompletionListener(listener)
        mPlayer!!.setOnErrorListener { mp, what, extra ->
            mPlayer!!.reset()
            false
        }
        try {
            mPlayer!!.setDataSource(filePath)
            mPlayer!!.prepare()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            throw RuntimeException("读取文件异常：" + e.message)
        }
        mPlayer!!.start()
        isPause = false
    }

    fun playAsset(fileName: String, context: Context, isLoop: Boolean) {
        try {
            if (mPlayer == null) {
                mPlayer = MediaPlayer()
            } else {
                mPlayer!!.reset()
            }
            mPlayer!!.setOnErrorListener { mp, what, extra ->
                mPlayer!!.reset()
                false
            }
            val assetFileDescriptor = context.assets.openFd(fileName)
            mPlayer!!.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset, assetFileDescriptor.length
            )
            mPlayer!!.isLooping = isLoop
            mPlayer!!.prepare()
            mPlayer!!.start()
            isPause = false
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            throw RuntimeException("读取文件异常：" + e.message)
        }
    }

    fun pause() {
        if (mPlayer != null && mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            isPause = true
        }
    }

    // 继续
    fun resume() {
        if (mPlayer != null && isPause) {
            mPlayer!!.start()
            isPause = false
        }
    }

    fun release() {
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
        }
    }
}