package com.GoldenMine;

import com.GoldenMine.objects.*;
import com.GoldenMine.objects.notes.LongNote;
import com.GoldenMine.objects.notes.Note;
import com.GoldenMine.objects.notes.SingleNote;
import com.GoldenMine.thread.threadAPI.APISingleThread;
import com.GoldenMine.thread.threadAPI.APIThread;
import com.GoldenMine.thread.threadAPI.APIThreadHandler;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javazoom.jl.player.Player;

/**
 * Created by ehe12 on 2018-11-12.
 */
public class RhythmDisplay extends JFrame {
    private static final byte[] hitSound;

    static {
        File file = new File("resources/hitsound.mp3");
        hitSound = new byte[(int) file.length()];
        try {
            InputStream is = new FileInputStream(file);
            int readed = 0;

            while (is.available() > 0) {
                int read = is.read();

                if (read != -1) {
                    hitSound[readed] = (byte) read;
                    readed++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    APISingleThread thread;

    BufferedImage buffer;
    Graphics bufferGraphics;

    List<List<Note>> notes = new ArrayList<>();

    GameInformation information;
    JudgementPrinter judgementPrinter;
    ComboPrinter comboPrinter;
    ScorePrinter scorePrinter;
    PressPrinter pressPrinter;

    List<Drawer> drawers = new ArrayList<>();

    Judgement lastJudgement;
    long lastJudgementTime; // 1초동안 보여주자

    int combo;
    long lastComboTime;

    int fps;
    int finfps;

    long score;

    boolean started;
    //boolean autoMode;

    HashMap<Integer, Boolean> pressed = new HashMap<>();



    public synchronized void combo(Judgement judgement) {
        combo++;
        lastJudgement = judgement;
        lastJudgementTime = System.currentTimeMillis();
        lastComboTime = System.currentTimeMillis();

        scoring(judgement);
    }

    public void onPress(int line) {
        //System.out.println("PRESS, " + System.currentTimeMillis());
        if (!pressed.containsKey(line)) {
            printHitSound();
            pressed.put(line, true);
        }

        if (!pressed.get(line)) {
            printHitSound();
            pressed.put(line, true);
        }

        List<Note> noteList = notes.get(line);
        int linePressed = information.getNoteLinePressed(line);

        if (noteList.size() > linePressed) {

            Note note = noteList.get(linePressed);

            Judgement judgement;

            //synchronized (note)
            {
                judgement = note.onPress(line, information);
            }
            if (judgement != Judgement.MISS) {
                if (judgement != Judgement.NONE) {
                    combo(judgement);
                }
            } else {
                MISS();
            }

        }
    }

    public void onRelease(int line) {
        //System.out.println("RELEASE, " + System.currentTimeMillis());
        if (pressed.get(line))
            pressed.put(line, false);

        List<Note> noteList = notes.get(line);
        int linePressed = information.getNoteLinePressed(line);

        if (noteList.size() > linePressed) {
            Note note = noteList.get(linePressed);

            Judgement judgement;

            //synchronized (note)
            {
                judgement = note.onRelease(line, information);
            }
            //judgementPrinter.drawJudgement(bufferGraphics, judgement, information);
            if (judgement != Judgement.MISS) {
                if (judgement != Judgement.NONE) {
                    combo(judgement);
                }
            } else {
                MISS();
            }

        }
    }

    public void printHitSound() {
        new Thread() {
            public void run() {
                try {
                    byte[] copy = new byte[hitSound.length];
                    for (int i = 0; i < copy.length; i++) {
                        copy[i] = hitSound[i];
                    }
                    ByteArrayInputStream bai = new ByteArrayInputStream(copy);

                    Player player = new Player(bai);
                    player.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public synchronized void scoring(Judgement judgement) {
        float comboMultiply = (float) Math.pow(1.05, Math.floor(combo / 10));

        score += comboMultiply * judgement.getScore();
        //System.out.println(score);
    }

    public RhythmDisplay(int fps, GameInformation information) {
        buffer = new BufferedImage(information.getWindowSize().getX(), information.getWindowSize().getY(), BufferedImage.TYPE_INT_ARGB);
        thread = new APISingleThread(fps, new APIThreadHandler() {
            @Override
            public void onThreadExecute() throws InterruptedException {
                RhythmDisplay.this.fps++;
                repaint();
            }
        });

        judgementPrinter = new JudgementPrinter();
        comboPrinter = new ComboPrinter();
        scorePrinter = new ScorePrinter();
        pressPrinter = new PressPrinter();


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!information.getAutoMode()) {
                    if (information.hasKeySetting(e.getKeyChar())) {
                        int line = information.getKeySetting(e.getKeyChar()) - 1;
                        onPress(line);
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!information.getAutoMode()) {
                    if (information.hasKeySetting(e.getKeyChar())) {
                        int line = information.getKeySetting(e.getKeyChar()) - 1;

                        onRelease(line);
                    }
                }
                //information.addNoteLinePressed(line);
            }
        });

        APIThread thread2 = new APISingleThread(1, new APIThreadHandler() {
            @Override
            public void onThreadExecute() throws InterruptedException {
                finfps = RhythmDisplay.this.fps;
                RhythmDisplay.this.fps = 0;
            }
        });
        thread2.start();

        setSize(information.getWindowSize().getX(), information.getWindowSize().getY());

        bufferGraphics = buffer.getGraphics();
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.information = information;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAllData();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                saveAllData();
                System.exit(0);
            }
        });
    }

    public void registerDrawer(Drawer drawer) {
        drawers.add(drawer);
    }

    public void saveAllData() {

    }

    public void renderMusic(Music music, int offset) {
        List<List<Note>> notes = new ArrayList<>();

        for (int i = 0; i < information.getNoteLineAmount(); i++) {
            notes.add(new ArrayList<>());
        }

        for (Note note : music.getNotes()) {
            notes.get(note.getLine() - 1).add(note);
        }

        this.notes = notes;

        //System.out.println(notes);
        long start = information.gameStart(3000);
        long start2 = music.playMP3(3000 + offset);

        long ocha = start2 - start;
        music.skipMS(ocha);
    }

    public void start() {
        setVisible(true);
        thread.start();
    }

    public void filterMissNote() {
        //System.out.println(notes.size());
        for (int index = 0; index < notes.size(); index++) {
            List<Note> notes = this.notes.get(index);
            int pressed = information.getNoteLinePressed(index);

            //for(int noteIndex = pressed; noteIndex < notes.size(); noteIndex++)
            if (notes.size() > pressed) {
                Note note = notes.get(pressed);

                if (note.verifyMiss(information)) {
                    information.addNoteLinePressed(index);
                    MISS();
                }
            }
        }
    }

    public void processAuto() {
        if (information.getAutoMode()) {
            for (int index = 0; index < notes.size(); index++) {
                List<Note> notes = this.notes.get(index);
                int pressed = information.getNoteLinePressed(index);


                //for(int noteIndex = pressed; noteIndex < notes.size(); noteIndex++)
                if (notes.size() > pressed) {
                    Note note = notes.get(pressed);

                    if (note instanceof SingleNote) {

                        int inputMS = information.getInputMS()+20;
                        int noteInputMS = note.getInputPos();

                        if (noteInputMS <= inputMS) {
                            new Thread() {
                                public void run() {

                                    onPress(note.getLine()-1);
                                    /*try {
                                        Thread.sleep(20L);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/
                                    onRelease(note.getLine()-1);
                                }
                            }.start();
                            //information.addNoteLinePressed(index);
                        }
                    } else if(note instanceof LongNote){
                        LongNote lNote = (LongNote)note;

                        int inputMS = information.getInputMS()+10;
                        int noteInputMS = lNote.getStartMS();

                        if (noteInputMS <= inputMS) {
                            onPress(note.getLine()-1);
                            //information.addNoteLinePressed(index);
                        }
                        int noteInputFinishMS = lNote.getFinishMS();

                        if (noteInputFinishMS <= inputMS) {
                            onRelease(note.getLine()-1);
                            //information.addNoteLinePressed(index);
                        }
                    }
                }
            }
        }
    }

    public void MISS() {
        combo = 0;
        lastJudgement = Judgement.MISS;
        lastJudgementTime = System.currentTimeMillis();
        lastComboTime = System.currentTimeMillis();
    }

    @Override
    public void paint(Graphics g) {
        if (notes != null) {

            bufferGraphics.setColor(Color.BLACK);
            bufferGraphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());


            //bufferGraphics.setColor(Color.BLACK);
            for (List<Note> noteList : notes) {
                for (Note note : noteList)
                    note.drawOnCanvas(this.bufferGraphics, information);
            }

            for (Drawer drawer : drawers) {
                drawer.drawOnCanvas(this.bufferGraphics, information);
            }

            processAuto();
            filterMissNote();
            double percent = (System.currentTimeMillis() - lastJudgementTime) / 500D;
            if (0 <= percent && percent <= 1)
                judgementPrinter.drawJudgement(bufferGraphics, lastJudgement, information, percent);

            double percent2 = (System.currentTimeMillis() - lastComboTime) / 500D;
            if (percent2 >= 0) {
                comboPrinter.drawCombo(bufferGraphics, combo, information, lastComboTime, percent2);
                comboPrinter.drawComboStr(bufferGraphics, combo, information, lastComboTime, percent2);
            }
            scorePrinter.drawScore(bufferGraphics, score, information);

            bufferGraphics.setFont(new Font("나눔고딕", 0, 30));
            bufferGraphics.setColor(Color.WHITE);
            bufferGraphics.drawString("FPS: " + finfps, 20, 70);

            pressPrinter.onPress(bufferGraphics, pressed, information);

            g.drawImage(buffer, 0, 0, null);
        }

        //super.paint(bufferGraphics);
    }


}
