/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stiglmair.com.hydra.listener;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

/**
 *
 * @author PogChamp This class handles all audio events.
 */
public class AudioListener {

    @EventSubscriber
    public void onAudioPlayed(TrackFinishEvent event) {
    }
}
