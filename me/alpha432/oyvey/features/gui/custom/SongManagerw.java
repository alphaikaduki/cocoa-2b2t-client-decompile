package me.alpha432.oyvey.features.gui.custom;

import java.util.List;
import net.minecraft.client.audio.ISound;

public class SongManagerw implements Globals {

    private final List songs;
    private final ISound menuSong;
    private ISound currentSong;

    public SongManagerw(List songs, ISound menuSong) {
        this.songs = songs;
        this.menuSong = menuSong;
    }

    public void getMenuSong() {}

    public void skip() {}

    public void play() {}

    public void stop() {}

    private void isCurrentSongPlaying() {}

    public void shuffle() {}

    private void getRandomSong() {}
}
