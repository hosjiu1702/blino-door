package com.example.hosjiu.blinoapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {

    private int maxStreams_;
    private Context mContext;

    private int connectingResId = R.raw.connect;
    private int connectingSoundId;
    private int connectingStreamId;

    private int finishingResId = R.raw.finish_connect;
    private int finishingSoundId;
    private int finishingStreamId;

    private int invitingResId = R.raw.invite_human;
    private int invitingSoundId;
    private int invitingStreamId;

    private int openDoorResId = R.raw.open_door;
    private int openDoorSoundId;
    private int openDoorStreamId;

    private int closeDoorResId = R.raw.close_door;
    private int closeDoorSoundId;
    private int closeDoorStreamId;

    private int stopDoorResId = R.raw.stop_door;
    private int stopDoorSoundId;
    private int stopDoorStreamId;

    private SoundPool mSoundPool = null;
    private AudioAttributes audioAttributes = null;

    private boolean isLoaded = false;

    public SoundManager(int maxStreams, Context context) {
        maxStreams_ = maxStreams;
        mContext = context;

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(maxStreams_)
                .setAudioAttributes(audioAttributes)
                .build();

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoaded = true;
            }
        });

        connectingSoundId = mSoundPool.load(mContext, connectingResId, 1);
        finishingSoundId = mSoundPool.load(mContext, finishingResId, 1);
        invitingSoundId = mSoundPool.load(mContext, invitingResId, 1);
        openDoorSoundId = mSoundPool.load(mContext, openDoorResId, 1);
        closeDoorSoundId = mSoundPool.load(mContext, closeDoorResId, 1);
        stopDoorSoundId = mSoundPool.load(mContext, stopDoorResId, 1);
    }

    public int playConnectSound(){
        if(isLoaded){
            connectingStreamId = mSoundPool.play(
                    connectingSoundId,
                    0.6f,
                    0.6f,
                    1,
                    -1,
                    1.5f);
        }
        return connectingStreamId;
    }

    public int playFinishSound(){
        if(isLoaded){
            finishingStreamId = mSoundPool.play(
                    finishingSoundId,
                    0.8f,
                    0.8f,
                    1,
                    0,
                    1.0f);
        }
        return finishingStreamId;
    }

    public int playInviteSound(){
        if(isLoaded){
            invitingStreamId = mSoundPool.play(
                    invitingSoundId,
                    1.0f,
                    1.0f,
                    1,
                    0,
                    1.0f);
        }
        return invitingStreamId;
    }

    public int playOpenDoorSound(){
        if(isLoaded){
            openDoorStreamId = mSoundPool.play(
                    openDoorSoundId,
                    1.0f,
                    1.0f,
                    1,
                    0,
                    1.0f);
        }
        return openDoorStreamId;
    }

    public int playCloseDoorSound(){
        if(isLoaded){
            closeDoorStreamId = mSoundPool.play(
                    closeDoorSoundId,
                    1.0f,
                    1.0f,
                    1,
                    0,
                    1.0f);
        }
        return closeDoorStreamId;
    }

    public int playStopDoorSound(){
        if(isLoaded){
            stopDoorStreamId = mSoundPool.play(
                    stopDoorSoundId,
                    1.0f,
                    1.0f,
                    1,
                    0,
                    1.0f);
        }
        return stopDoorStreamId;
    }

    public void pauseConnectingSound() {
        mSoundPool.pause(connectingStreamId);
    }

    public void stopOpenDoorSound() {
        mSoundPool.stop(openDoorStreamId);
    }

    public void stopCloseDoorSound() {
        mSoundPool.stop(closeDoorStreamId);
    }

    public void stopStopDoorSound() {
        mSoundPool.stop(stopDoorStreamId);
    }

    public void pauseFinishSound() {
        mSoundPool.pause(finishingStreamId);
    }

    public void release() {
        mSoundPool.release();
    }
}
