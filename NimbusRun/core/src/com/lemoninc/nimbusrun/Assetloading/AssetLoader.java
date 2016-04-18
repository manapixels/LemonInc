package com.lemoninc.nimbusrun.Assetloading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class AssetLoader {
    //buttons
    public static Texture muteButton;
    public static Texture unmuteButton;
    //styles

    //sounds
    public static Music gameMusic;
    public static Music menuMusic;
    public static Sound clickSound;
    public static Music walkSound;
    public static Music runSound;
    private static ArrayList<Music> musicBox;
    private static ArrayList<Sound> soundBox;
    public static float VOLUME;

    public static void initiate(){
        VOLUME = 1;
        musicBox = new ArrayList<Music>();
        soundBox = new ArrayList<Sound>();
    }

    public static void loadALL(){
        loadMenuSfx();
    }
    public static void loadMenuSfx() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/puppetry_comedy.mp3"));
        musicBox.add(menuMusic);
        menuMusic.setLooping(true);
    }

    public static void muteSFX() {
        for (Sound s : soundBox) {
            s.setVolume(0, 0f);
        }
        for (Music m : musicBox) {
            m.setVolume(0f);
        }
        VOLUME = 0f;
    }

    public static void unmuteSFX() {
        for (Sound s : soundBox) {
            s.setVolume(0, 1f);
        }
        for (Music m : musicBox) {
            m.setVolume(1f);
        }
        VOLUME = 1f;
    }

    public static void dispose(){
        try {
            muteButton.dispose();
            unmuteButton.dispose();
            walkSound.dispose();
            runSound.dispose();
            // Dispose Sound
            menuMusic.dispose();
            clickSound.dispose();
            walkSound.dispose();
            runSound.dispose();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void disposeSFX(){
        try {
            gameMusic.dispose();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
