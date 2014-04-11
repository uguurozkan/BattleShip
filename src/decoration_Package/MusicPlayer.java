package decoration_Package;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/** MusicPlayer Class */

public class MusicPlayer implements Serializable {
	private AudioStream audioStream = null;
	private String songFile;
	private InputStream input = null;

	public MusicPlayer(String songName, String type) {
		this.songFile = "src\\" + songName + "." + type;
		initMusicPlayer();
	}

	public String getSongFile() {
		return songFile;
	}

	public void setInput() {
		try {
			this.input = new FileInputStream(getSongFile());
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		}
	}

	public InputStream getInput() {
		return this.input;
	}

	public void setAudioStream() {
		try {
			this.audioStream = new AudioStream(getInput());
		} catch (IOException e) {
			System.out.println("Do not find audio stream!");
		}
	}

	public AudioStream getAudioStream() {
		return audioStream;
	}

	public void initMusicPlayer() {
		setInput();
		setAudioStream();
	}

	public void runPlayer() {
		initMusicPlayer();
		AudioPlayer.player.start(getAudioStream());
	}

	public void stopPlayer() {
		AudioPlayer.player.stop(getAudioStream());
	}

}
