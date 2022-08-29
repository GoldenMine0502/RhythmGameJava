package kr.goldenmine.objects;

import kr.goldenmine.GameInformation;
import java.awt.Graphics;

/**
 * Created by ehe12 on 2018-11-13.
 */
public interface Drawer {

    void drawOnCanvas(Graphics g, GameInformation information);
}
