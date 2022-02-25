package com.east.east_utils.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by EastRiseWM on 2017/3/8.
 */

public class AudioUtils {
    public static AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            //KLog.e(focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {/*说明你临时失去了音频焦点，但是在不久就会再返回来。此时，你必须终止所有的音频播放
                                                                        ，但是保留你的播放资源，因为可能不久就会返回来。*/
//                if(mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                }

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {/*你已经获得音频焦点；*/
//                if(mediaPlayer==null){
//                    initBeepSound();
//                }else if(!mediaPlayer.isPlaying()){
//
//                    mediaPlayer.start();
//
//                }
                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {/*你已经失去音频焦点很长时间了，必须终止所有的音频播放。
                                                                                因为长时间的失去焦点后，不应该在期望有焦点返回，
                                                                            这是一个尽可能清除不用资源的好位置。例如，应该在此时释放MediaPlayer对象；*/
//                if(mediaPlayer.isPlaying()){
//
//                    mediaPlayer.stop();
//                }
//                am.abandonAudioFocus(afChangeListener);
                // Stop playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { /* 永久获取媒体焦点（播放音乐）*/
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.stop();
//                }

            } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
//                if(mediaPlayer.isPlaying()){
//                    mediaPlayer.stop();
//                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {/*这说明你已经临时失去了音频焦点，但允许你安静的播放音频（低音量）
                                                                    ，而不是完全的终止音频播放。目前所有的情况下，oFocusChange的时候停止mediaPlayer */

            }
        }
    };

    /*获取焦点*/
    public static void requestFocus(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC, // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
        //KLog.e(result);
//        //KLog.e(audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_VOICE_CALL,AudioManager.AUDIOFOCUS_GAIN));
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            // Start playback. // 开始播放音乐文件
        }
    }

    /*放弃焦点*/
    public static void abandon(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(afChangeListener);
    }


}
