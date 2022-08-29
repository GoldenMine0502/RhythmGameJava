package kr.goldenmine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ehe12 on 2018-11-12.
 */
public class GameInformation {
    List<Point> judgementLines;
    int startY;
    int finishY;

    Point windowSize;
    Point noteSize = new Point(20, 12);

    long startms;

    int multiply = 1; // 배속1 = 초당 20%

    int perfectY;

    int noteLineSizeX;
    int noteLineAmount;

    int[] notePressed;

    boolean autoMode;

    public void setAuto(boolean auto) {
        autoMode = auto;
    }

    public boolean getAutoMode() {
        return autoMode;
    }

    HashMap<Character, Integer> keySetting = new HashMap<>();

    public GameInformation() {
        judgementLines = new ArrayList<>();
        keySetting.put('c', 1);
        keySetting.put('v', 2);
        keySetting.put('m', 3);
        keySetting.put(',', 4);
    }

    public void setKeySetting(char ch, int line) {
        keySetting.put(ch, line);
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public long gameStart(int sleep) {
        startms = System.currentTimeMillis() + sleep; // sleep초후 게임시작
        for(int i = 0; i < notePressed.length; i++) {
            notePressed[i] = 0;
        }

        return startms;
    }

    public void setPerfectY(int perfectY) {
        this.perfectY = perfectY;
    }

    public void setWindowSize(Point size) {
        this.windowSize = size;
    }

    public void addPoint(Point p) {
        judgementLines.add(p);
    }

    public void setStartY(int y) {
        startY = y;
    }

    public void setFinishY(int y) {
        finishY = y;
    }

    public int getRealCenterYPosition(int currentPos) {
        //currentPos = 0 -> 5초후 perfectY라인
        long elapsed = getInputMS();

        //System.out.println(elapsed);
        //System.out.println(((elapsed-currentPos)/1000D * getOneSecondDrop()));

        return (int) (perfectY + ((elapsed-currentPos)/1000D * getOneSecondDrop()));
    }

    public void setNoteSize(Point p) {
        noteSize = new Point(p);
    }

    public void setNoteLineSizeX(int x) {
        noteLineSizeX = x;
        notePressed = new int[x];
    }

    public void setNoteLineAmount(int amount) {
        noteLineAmount = amount;
    }

    public int getRealCenterXPosition(int note) {
        return windowSize.getX()/2 + noteLineSizeX/2 + (note - noteLineAmount + 1) * noteLineSizeX;
    }

    public int getOneSecondDrop() {
        return windowSize.getY()/5*multiply;
    }

    public static void main(String[] args) throws InterruptedException {
        GameInformation info = new GameInformation();
        info.setWindowSize(new Point(500, 500));
        info.setPerfectY(450);
        info.setMultiply(1);
        info.gameStart(0);

        Thread.sleep(100);
        System.out.println("t: " + info.getRealCenterYPosition(0));
        System.out.println("t: " + info.getRealCenterYPosition(1000));
    }

    public Point getWindowSize() {
        return windowSize;
    }

    public Point getNoteSize() {
        return noteSize;
    }

    public int getPerfectY() {
        return perfectY;
    }

    public int getNoteLineAmount() {
        return noteLineAmount;
    }

    public int getKeySetting(char keyChar) {
        return keySetting.get(keyChar);
    }

    public int getNoteLinePressed(int line) {
        return notePressed[line];
    }

    public void addNoteLinePressed(int line) {
        synchronized (notePressed) {
            notePressed[line]++;
        }
    }

    public int getInputMS() {
        return (int) (System.currentTimeMillis()-startms);
    }

    public boolean hasKeySetting(char keyChar) {
        return keySetting.containsKey(keyChar);
    }

    public long getStartTime() {
        return startms;
    }
}
