package kr.goldenmine.objects.notes;

import kr.goldenmine.GameInformation;
import kr.goldenmine.Judgement;
import kr.goldenmine.objects.Drawer;

/**
 * Created by ehe12 on 2018-11-12.
 */
public interface Note extends Drawer {

    Judgement onPress(int line, GameInformation information);
    Judgement onRelease(int line, GameInformation information);

    void press(GameInformation information);
    boolean isPressed();

    int getLine();

    int getInputPos();

    boolean verifyMiss(GameInformation information);

    public default Judgement judgement(int inputPos, int noteInputPos, GameInformation information) {
        double error = Math.abs(inputPos-noteInputPos)/10D;

        Judgement result = null;

        //System.out.println(this.inputPos + ", " + inputPos + ", " + error);

        //System.out.println(inputPos + ", " + noteInputPos + ", " + error);

        for(Judgement judgement : Judgement.values()) {
            if(judgement.getJudgement()>=error) {
                result = judgement;
                break;
            }
        }

        return result;
    }
}
