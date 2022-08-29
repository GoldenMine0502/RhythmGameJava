package kr.goldenmine.objects;

import kr.goldenmine.GameInformation;
import kr.goldenmine.ImageUtility;
import kr.goldenmine.Judgement;
import kr.goldenmine.Point;

import java.awt.*;

/**
 * Created by ehe12 on 2018-11-13.
 */
public class JudgementPrinter {
    private static final Font nanum = new Font("나눔고딕", 0, 60);

    public void drawJudgement(Graphics g, Judgement type, GameInformation information, double percent) {
        if(type!=Judgement.NONE) {
            Graphics2D g2d = (Graphics2D)g;
            Point size = ImageUtility.getTextSize(nanum, type.toString());
            int startX = (information.getWindowSize().getX()-size.getX())/2;
            int startY = (int) (information.getWindowSize().getY()/4D*2.8);

            Font originalFont = g2d.getFont();
            Composite originalComposite = g2d.getComposite();
            g2d.setFont(nanum);
            if(percent < 0.4) {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) percent/0.4F));

            } else if(percent < 0.8){

            } else {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) ((1-(percent))*5)));
            }
            g2d.setColor(type.getColor());
            g2d.drawString(type.toString(), startX, startY);

            g2d.setComposite(originalComposite);
            g2d.setFont(originalFont);
            g2d.setColor(Color.BLACK);
        }
    }
}
