package com.GoldenMine.objects.notes;

import com.GoldenMine.GameInformation;
import com.GoldenMine.Judgement;
import com.GoldenMine.Point;
import java.awt.*;

/**
 * Created by ehe12 on 2018-11-12.
 */
public class SingleNote implements Note {
    int inputPos;
    int noteLine;

    boolean pressed;

    long pressTime = 0;
    int pressY;

    public SingleNote(int inputPos, int noteLine) {
        this.inputPos = inputPos;
        this.noteLine = noteLine;
    }

    public Judgement pressJudgment(int inputPos, GameInformation information) {
        return judgement(inputPos, getInputPos(), information);
    }

    public Judgement releaseJudgement(int inputPos, GameInformation information) {
        return Judgement.NONE;
    }

    @Override
    public Judgement onPress(int line, GameInformation information) {
        Judgement judgement = pressJudgment(information.getInputMS(), information);
        //System.out.println(line + " judgement(release):" + );
        //System.out.println(line + " judgement(press) : " + information.getNoteLinePressed(line) + ", " + judgement);

        if (judgement != Judgement.NONE) {
            press(information);

        }

        return judgement;
    }

    @Override
    public Judgement onRelease(int line, GameInformation information) {
        if (isPressed()) {
            //System.out.println(information.getNoteLinePressed(line));
            information.addNoteLinePressed(line);
        } else {
            Judgement judgement = releaseJudgement(information.getInputMS(), information);

            //System.out.println(line + " judgement(release):" + information.getNoteLinePressed(line) + ", " + judgement);

            if (judgement != Judgement.NONE) {
                press(information);
                information.addNoteLinePressed(line);
            }
        }

        return Judgement.NONE;
    }

    @Override
    public void press(GameInformation information) {
        if(pressTime==0) {
            pressTime = System.currentTimeMillis();
            pressY = information.getRealCenterYPosition(inputPos);
        }
        pressed = true;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    public int getLine() {
        return noteLine;
    }

    @Override
    public int getInputPos() {
        return inputPos;
    }

    @Override
    public boolean verifyMiss(GameInformation information) {
        if (!isPressed()) {
            int pos = information.getRealCenterYPosition(getInputPos()) - information.getPerfectY();

            if (pos >= Judgement.BAD.getJudgement() * 10) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void drawOnCanvas(Graphics g, GameInformation information) {


        //if(!pressed)
        {
            Color original = g.getColor();

            switch(information.getNoteLineAmount()) {
                case 4:
                    switch(noteLine) {
                        case 1:case 4:
                            g.setColor(Color.WHITE);
                            break;
                        case 2:case 3:
                            g.setColor(Color.PINK);
                            break;
                    }
                    break;
            }
            int xPos = information.getRealCenterXPosition(noteLine);
            int yPos;

            if(isPressed()) {
                yPos = pressY;
            } else {
                yPos = information.getRealCenterYPosition(inputPos);
            }

            Point p = information.getNoteSize();



            if (yPos + p.getY() > 0 && yPos - p.getY() < information.getWindowSize().getY()) {
                Graphics2D g2d = (Graphics2D) g;

                Composite originalComposite = g2d.getComposite();
                if(isPressed()) {
                    float currentComposite = 1F-(System.currentTimeMillis()-pressTime)/200F;

                    if(currentComposite>=0 && currentComposite<=1) {
                        g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, currentComposite)); // currrentComposite
                    } else if(currentComposite<=0) {
                        g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 0));
                    }
                }

                g.fillRect(xPos - p.getX() / 2, yPos - p.getY() / 2, p.getX(), p.getY());

                if(isPressed()) {
                    g2d.setComposite(originalComposite);
                }
            }

            g.setColor(original);
        }
    }

    @Override
    public String toString() {
        return "{inputPos : " + inputPos + ", noteLine : " + noteLine + "}";
    }
}
