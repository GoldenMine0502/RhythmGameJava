package com.GoldenMine.objects;

import com.GoldenMine.GameInformation;
import com.GoldenMine.ImageUtility;
import com.GoldenMine.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Created by ehe12 on 2018-11-14.
 */
public class ScorePrinter {
    private static final Font nanum = new Font("나눔고딕", Font.BOLD, 60);

    int lastScore;

    public void drawScore(Graphics g, long score, GameInformation information) {
        Color original = g.getColor();
        Font font = g.getFont();

        g.setColor(Color.WHITE);
        g.setFont(nanum);

        String scoreToStr = String.valueOf(score);
        StringBuilder scoreToStrSum = new StringBuilder();

        for(int i = scoreToStr.length(); i < 8; i++) {
            scoreToStrSum.append(0);
        }
        scoreToStrSum.append(scoreToStr);


        Point size = ImageUtility.getTextSize(nanum, scoreToStrSum.toString());
        int startX = information.getWindowSize().getX()-size.getX()-15;
        int startY = 75;

        g.drawString(scoreToStrSum.toString(), startX, startY);
        g.setFont(font);

        g.setColor(original);
    }
}
