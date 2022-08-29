package kr.goldenmine;

import kr.goldenmine.objects.JudgementLine;
import java.io.File;

/**
 * Created by ehe12 on 2018-11-12.
 */
public class Main {
    public static void main(String[] args) {
        GameInformation information = new GameInformation();
        information.setWindowSize(new Point(1700, 1000));
        information.setMultiply(10);
        information.setPerfectY(900);
        information.setNoteLineAmount(4);
        information.setNoteLineSizeX(180);
        information.setStartY(0);
        information.setFinishY(800);
        information.setNoteSize(new Point(120, 50));
        information.setAuto(true);

        Music music = new Music();
        music.loadFromOsuFile(new File("resources/clai/ETIA. - Claiomh Solais (-SoraGami-) [4K Normal].osu"));
        //music.loadFromOsuFile(new File("resources/dragon/DragonForce - The Warrior Inside (_underjoy) [4K Collab Gladiator].osu"));
        //music.loadFromOsuFile(new File("resources/kimiiro/Haruna Luna - Kimiiro Signal-TV size ver.- ([-Star_Lenz-]) [Ichi's 4K MX 'Saenai'].osu"));
        music.setMP3Path(new File("resources/clai/ETIA. - Claiomh Solais (-SoraGami-) [4K Insane].mp3"));
        //music.setMP3Path(new File("resources/dragon/Dragonforce - The Warrior Inside.mp3"));
        //music.setMP3Path(new File("resources/kimiiro/kimiiro signal.mp3"));
        RhythmDisplay display = new RhythmDisplay(240, information);
        //display.setFocusableWindowState(false);
        display.registerDrawer(new JudgementLine());
        display.start();

        display.renderMusic(music, -50);
        //display.renderMusic(music, 0);
        //display.renderMusic(music);
    }
}
