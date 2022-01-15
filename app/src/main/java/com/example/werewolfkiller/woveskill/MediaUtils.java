package com.example.werewolfkiller.woveskill;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.werewolfkiller.R;

import static android.content.ContentValues.TAG;

public class MediaUtils {
    public static MediaPlayer player;
    Activity activity;

    public MediaUtils(Activity activity) {
        this.activity = activity;
    }
    public void open(int num){
        player.reset();
        switch (num){
            case 4:
                player= MediaPlayer.create(activity, R.raw.swopen);
                break;
            case 5:
                player= MediaPlayer.create(activity,R.raw.nwopen);
                break;
            case 6:
                player= MediaPlayer.create(activity,R.raw.yyjopen);
                break;
            case 7:
                player= MediaPlayer.create(activity,R.raw.wolfopen);
                break;
        }
        MediaUtils.Starter();
    }
    public void close(int num){
        player.reset();
        switch (num){
            case 4:
                player= MediaPlayer.create(activity,R.raw.swclose);
                break;
            case 5:
                player= MediaPlayer.create(activity,R.raw.nwclose);
                break;
            case 6:
                player= MediaPlayer.create(activity,R.raw.yyjclose);
                break;
            case 7:
                player= MediaPlayer.create(activity,R.raw.wolfclose);
                break;
        }
        MediaUtils.Starter();
    }
    public void dayclose(){
        Log.d(TAG, "dayclose: ");
         player = MediaPlayer.create(activity, R.raw.dayclose);
         MediaUtils.Starter();
    }
    public void dayopen2(){
        player.reset();
        player = MediaPlayer.create(activity, R.raw.day_open_no_police);
        MediaUtils.Starter();
    }
    public void dayopen1(){
        player.reset();
        player = MediaPlayer.create(activity, R.raw.dayopen);
        MediaUtils.Starter();
    }

    private MediaPlayer.OnCompletionListener completionListener() {
        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion: ");
                releaseMediaPlayer();
            }
        };
    }
    public void setCompletlisen(){
        player.setOnCompletionListener(completionListener());
    }

   public void releaseMediaPlayer(){

        if (player!=null){

            player.release();
            player=null;
        }


    }
    public long getDuration(){
        return player.getDuration();
    }
    private static void Starter(){

                player.start();
                player.setLooping(false);
    }

}
