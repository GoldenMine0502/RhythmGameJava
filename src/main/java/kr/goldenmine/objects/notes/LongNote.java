package kr.goldenmine.objects.notes;

import kr.goldenmine.GameInformation;
import kr.goldenmine.Judgement;

import java.awt.*;

/**
 * Created by ehe12 on 2018-11-12.
 */
public class LongNote implements Note {
    int startPos;
    int finishPos;
    int noteLine;

    boolean pressed;

    boolean released;

    boolean isMiss;
    long isMissTime;

    long pressTime = 0;
    int pressY;

    public LongNote(int startPos, int finishPos, int noteLine) {
        this.startPos = startPos;
        this.finishPos = finishPos;
        this.noteLine = noteLine;
    }

    public Judgement pressJudgement(int inputPos, GameInformation information) {
        if (!pressed) {
            return judgement(inputPos, startPos, information);
        } else {
            return Judgement.NONE;
        }
    }

    public Judgement releaseJudgment(int inputPos, GameInformation information) {
        if (pressed) {
            released = true;
            if (pressTime == 0) {
                pressTime = System.currentTimeMillis();
                pressY = information.getRealCenterYPosition(inputPos);
            }
        }
        return judgement(inputPos, finishPos, information);
    }

    public int getStartMS() {
        return startPos;
    }

    public int getFinishMS() {
        return finishPos;
    }

    @Override
    public Judgement onPress(int line, GameInformation information) {
        int inputMS = information.getInputMS();

        Judgement judgement = pressJudgement(inputMS, information);
        //System.out.println(line + " judgement(release):" + );

        if (judgement != Judgement.NONE) {
            //System.out.println(line + " judgement(press-long) : " + information.getNoteLinePressed(line) + ", " + judgement);
            press(information);
        } else if (startPos <= inputMS && inputMS <= finishPos) {
            if (!isPressed()) {
                //System.out.println(line + " judgement(press-long2) : " + information.getNoteLinePressed(line) + ", " + judgement);

                judgement = Judgement.BAD;
                press(information);
            }
        }

        //System.out.println(line + ": " + inputMS + ", " + startPos + ", " + judgement);

        return judgement;
    }

    @Override
    public Judgement onRelease(int line, GameInformation information) {
        int inputMS = information.getInputMS();

        boolean verify = false;

        Judgement judgement = releaseJudgment(inputMS, information);
        if (judgement == Judgement.NONE) {
            if (startPos - Judgement.BAD.getJudgement() * 10 <= inputMS) {
                judgement = Judgement.MISS;
                isMissTime = information.getStartTime() + inputMS;
                isMiss = true;
                verify = true;
            }
        } else {
            verify = true;
        }

        if(verify) {
            information.addNoteLinePressed(line);
            //System.out.println(line + " judgement(release-long):" + information.getNoteLinePressed(line) + ", " + judgement);
        }


        return judgement;
    }

    @Override
    public void press(GameInformation information) {
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
        if (!isMiss) {
            return finishPos;
        } else {
            return -999999999; // MISS 고의로 만듦
        }
    }

    @Override
    public boolean verifyMiss(GameInformation information) {
        if (!isPressed()) {
            int pos = information.getRealCenterYPosition(startPos) - information.getPerfectY();

            if (pos >= Judgement.BAD.getJudgement() * 10) {
                isMissTime = information.getStartTime() + information.getInputMS();
                //System.out.println(startPos + " MISS");
                isMiss = true;
                return true;
            }
        } else {
            if (released) {
                int pos = information.getRealCenterYPosition(getInputPos()) - information.getPerfectY();

                if (pos >= Judgement.BAD.getJudgement() * 10) {
                    isMissTime = information.getStartTime() + information.getInputMS();
                    isMiss = true;
                    //System.out.println(startPos + " MISS");
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void drawOnCanvas(Graphics g, GameInformation information) {
        Graphics2D g2d = (Graphics2D) g;

        //if(!released)
        {
            int xPos = information.getRealCenterXPosition(noteLine);
            int yPos = information.getRealCenterYPosition(startPos);

            int yPos2 = information.getRealCenterYPosition(finishPos);

            //int windowY = information.getWindowSize().getY();


            //if((yPos > 0 && yPos < windowY) || (yPos2>0 && yPos2 < windowY))
            //{
            //System.out.println(yPos + ", " + yPos2);

            //g.setColor(Color.BLUE);

            Color original = g.getColor();

            switch (information.getNoteLineAmount()) {
                case 4:
                    switch (noteLine) {
                        case 1:
                        case 4:
                            g.setColor(Color.WHITE);
                            break;
                        case 2:
                        case 3:
                            g.setColor(Color.PINK);
                            break;
                    }
                    break;
            }

            int height;
            if (isPressed()) {
                //height = yPos - yPos2 + information.getNoteSize().getY();
                height = information.getPerfectY() - yPos2 - information.getNoteSize().getY() / 2;
            } else {
                height = yPos - yPos2;
            }

            boolean verify = false;

            if (yPos + information.getNoteSize().getY() > 0) {
                if (yPos2 - information.getNoteSize().getY() < information.getWindowSize().getY()) {
                    verify = true;
                }
            }

            Composite originalComposite = null;
            if (verify) {
                originalComposite = g2d.getComposite();
            }


            if (verify) {
                float currentComposite = 0;
                if (released) {
                    currentComposite = 1F - (System.currentTimeMillis() - pressTime) / 200F;
                } else if (isMiss) {
                    currentComposite = 1F - (System.currentTimeMillis() - isMissTime) / 200F;
                }

                if (released || isMiss) {
                    if (currentComposite >= 0 && currentComposite <= 1) {
                        g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, currentComposite)); // currrentComposite
                    } else if (currentComposite <= 0) {
                        g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 0));
                    }
                }
            } else {

            }


            if (verify) {
                g.fillRect(xPos - information.getNoteSize().getX() / 2, yPos2 - information.getNoteSize().getY() / 2, information.getNoteSize().getX(), height + information.getNoteSize().getY());
            }
            //g.setColor(Color.BLACK);

            //g.setColor(Color.BLUE);
            //    g.drawString(xPos + ", " + yPos + ", " + yPos2, xPos-100, yPos);
            //    g.setColor(Color.BLACK);
            //}

            g.setColor(original);

            if (verify) {
                g2d.setComposite(originalComposite);
            }
        }
    }

    @Override
    public String toString() {
        return "{startPos : " + startPos + ", finishPos: " + finishPos + ", noteLine : " + noteLine + "}";
    }
}
