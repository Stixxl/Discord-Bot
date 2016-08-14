package com.corbi.robot.actions;

import com.corbi.robot.main.Main;
import com.corbi.robot.utilities.UtilityMethods;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.impl.obj.VoiceChannel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stiglmair This class is designed to handle anything the bot wants to
 * say. Any Audiocommand should use this class.
 */
public class Audio {

    /**
     * retrieves path to soundfile
     *
     * @param args the arguments that were sent in addition to the command
     * @param textChannel the TextChannel, where a potential error message can
     * be printed
     * @param voiceChannels channel in which the user is, only the first will be
     * used
     * @param guild guild, in which the request was received
     * @return false, if the key does not exist or to many arguments were
     * received; true if the method could sucessfully execute the audio request
     */
    public static boolean handleSoundRequest(String[] args, IChannel textChannel, List<IVoiceChannel> voiceChannels, IGuild guild) throws DiscordException, HTTP429Exception, MissingPermissionsException {
        if ((args.length == 1 && !voiceChannels.isEmpty())) {
            String path = null;
            IVoiceChannel voiceChannel = null;
            for (IVoiceChannel channel : voiceChannels) {
                if (channel.getGuild().equals(guild))//true if channel is in the same guild as origin of request, false otherwise
                {
                    Logger.getGlobal().log(Level.FINER, "VoiceChannel on correct server found.");
                    voiceChannel = channel;
                }
            }
            try {
                path = Main.soundService.getPath(args[0]);//retrieves path for requested file and increments the overall call counter
                Main.soundService.incrementRequestAmount(args[0]);
            } catch (SQLException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Sound path could not be retrieved.", ex);
            }
            Logger.getGlobal().log(Level.FINER, "The retrieved audio path was: {0}", path);

            if (path != null && voiceChannel != null) {//true, if requested sound exists in database AND voiceChannel of user could be detected, false otherwise
                path = UtilityMethods.generatePath(path);
                playSound(path, voiceChannel, guild);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * plays a soundfile
     *
     * @param path absolute path to the audio file
     * @param voiceChannel channel, where the audio will be streamed
     */
    private static void playSound(String path, IVoiceChannel voiceChannel, IGuild guild) throws MissingPermissionsException {
        AudioPlayer audioPlayer = AudioPlayer.getAudioPlayerForGuild(guild);
        File file = new File(path);
        voiceChannel.join();
        try {
            audioPlayer.setLoop(false);
            audioPlayer.setVolume(0.35f);
            audioPlayer.queue(file);
        } catch (IOException | UnsupportedAudioFileException ex) {
            Logger.getGlobal().log(Level.SEVERE, "Error while trying to play audio.", ex);
            voiceChannel.leave();
        }
    }
}
