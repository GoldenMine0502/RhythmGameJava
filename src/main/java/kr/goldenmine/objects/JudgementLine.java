package kr.goldenmine.objects;

import kr.goldenmine.GameInformation;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by ehe12 on 2018-11-13.
 */
public class JudgementLine implements Drawer {
    @Override
    public void drawOnCanvas(Graphics g, GameInformation information) {
        g.setColor(Color.RED);
        g.fillRect(0, information.getPerfectY(), information.getWindowSize().getX(), information.getNoteSize().getY()/8);
        g.setColor(Color.BLACK);
    }
}
