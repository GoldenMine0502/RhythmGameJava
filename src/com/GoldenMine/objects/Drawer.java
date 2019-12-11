package com.GoldenMine.objects;

import com.GoldenMine.GameInformation;
import java.awt.Graphics;

/**
 * Created by ehe12 on 2018-11-13.
 */
public interface Drawer {

    void drawOnCanvas(Graphics g, GameInformation information);
}
