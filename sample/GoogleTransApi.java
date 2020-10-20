package sample;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import com.gtranslate.Translator;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class GoogleTransApi {
    public static String translate(String s) {
        Translator translate = Translator.getInstance();
        String text = translate.translate(s + "&client=tw-ob", Language.ENGLISH, Language.VIETNAMESE);
        return text;
    }

    public static void Speak(String s) {
        try {
            Audio audio = Audio.getInstance();
            InputStream sound = audio.getAudio(s + "&client=tw-ob", Language.ENGLISH);
            audio.play(sound);
        } catch (IOException | JavaLayerException e) {
            System.out.println(e.getMessage());
        }
    }

}
