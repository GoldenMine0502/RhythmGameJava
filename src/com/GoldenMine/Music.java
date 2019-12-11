package com.GoldenMine;

import com.GoldenMine.objects.notes.LongNote;
import com.GoldenMine.objects.notes.Note;
import com.GoldenMine.objects.notes.SingleNote;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * Created by ehe12 on 2018-11-13.
 */
public class Music {
    private List<Note> notes;
    private Player playMP3;

    public Music() {
        notes = new ArrayList<>();
    }

    public void loadFromOsuFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            while (!reader.readLine().equals("[HitObjects]")) ;

            String readed;

            while ((readed = reader.readLine()) != null) {
                String[] splitReaded = readed.split(",");
                /*
                64,192,1424,5,0,0:0:0:0:
                192,192,1583,1,0,0:0:0:0:
                320,192,1743,1,0,0:0:0:0:
                448,192,1902,1,0,0:0:0:0:


                448,192,19296,128,0,19775:0:0:0:0:
                64,192,19775,128,0,20253:0:0:0:0:
                192,192,20253,128,0,20572:0:0:0:0:
                448,192,20572,128,0,21211:0:0:0:0:
                320,192,21211,128,0,21849:0:0:0:0:
                 */

                //System.out.println(splitReaded.length);

                if(splitReaded.length<3)
                    break;

                int note = noteVerify(Integer.parseInt(splitReaded[0]));
                int offset = Integer.parseInt(splitReaded[2]);

                if(Integer.parseInt(splitReaded[3])!=128) { // if it is singleNote
                    notes.add(new SingleNote(offset, note));
                } else { // LongNote
                    int endOffect = Integer.parseInt(splitReaded[5].split(":")[0]);
                    notes.add(new LongNote(offset, endOffect, note));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setMP3Path(File file) {
        try {

            //this.setDurationLabel(durationInSeconds);

            playMP3 = new Player(new FileInputStream(file));
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public long playMP3(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            playMP3.play();
            return System.currentTimeMillis();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int noteVerify(int posX) {
        switch (posX) {
            case 64:
                return 1;
            case 192:
                return 2;
            case 320:
                return 3;
            case 448:
                return 4;
        }
        return -1;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void skipMS(long ocha) {

    }
}
