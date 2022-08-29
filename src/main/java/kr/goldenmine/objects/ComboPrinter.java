package kr.goldenmine.objects;

import kr.goldenmine.GameInformation;
import kr.goldenmine.ImageUtility;
import kr.goldenmine.Point;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ehe12 on 2018-11-14.
 */
public class ComboPrinter {
    private static final Font nanum = new Font("나눔고딕", 0, 80);

    private float lastPercentX = 1;
    private float lastPercentY = 1;
    //private float lastComposite = 1;

    private long lastStart = -1;

    private boolean lastMiss = false;
    private long lastMissTime = -1;

    public void drawComboStr(Graphics g, int combo, GameInformation information, long start, double percent) {
        Graphics2D g2d = (Graphics2D) g;
        AtomicInteger inte;

        String comboToStr = "COMBO";
        kr.goldenmine.Point size = ImageUtility.getTextSize(nanum, comboToStr);
        int startX = (information.getWindowSize().getX() - size.getX()) / 2;
        int startY = (int) ((information.getWindowSize().getY() / 4D * 1.2) + nanum.getSize()*1.2);

        Color originalColor = g2d.getColor();
        AffineTransform originalAffine = g2d.getTransform();
        Composite originalComposite = g2d.getComposite();
        Font originalFont = g2d.getFont();

        g2d.setColor(Color.WHITE);

        if (combo > 0) {
            lastMiss = false;
            if (percent < 0.3) {
                float currentPercentY = (float) (1 + percent * 1.6);
                float calculatedPercentY = currentPercentY;

                float currentPercentX = (float) (1 + percent * 0.6);
                float calculatedPercentX = currentPercentX;

                AffineTransform transform = new AffineTransform();
                transform.scale(calculatedPercentX, calculatedPercentY); // 1.15

                g2d.setTransform(transform);

                startX = ImageUtility.getResizedScalePoint(startX, size.getX(), calculatedPercentX);
                startY = ImageUtility.getResizedScalePoint(startY, -size.getY()/2, calculatedPercentY);

                lastPercentY = currentPercentY;
            } else if (percent < 0.6) {
                float currentPercentY = (float) (1 + (0.6 - percent) * 1.6);
                float calculatedPercentY = currentPercentY;

                float currentPercentX = (float) (1 + (0.6 - percent) * 0.6);
                float calculatedPercentX = currentPercentX;

                AffineTransform transform = new AffineTransform();
                transform.scale(calculatedPercentX, calculatedPercentY);
                g2d.setTransform(transform);
                startX = ImageUtility.getResizedScalePoint(startX, size.getX(), calculatedPercentX);
                startY = ImageUtility.getResizedScalePoint(startY, -size.getY()/2, calculatedPercentY);

                lastPercentY = currentPercentY;
            } else {

            }
        } else {
            float currentComposite;
            if (!lastMiss) {
                lastMiss = true;
                lastMissTime = start;
            }
            if (lastMissTime == start) {
                currentComposite = (float) (1 - percent);
            } else {
                currentComposite = 1 - ((System.currentTimeMillis() - lastMissTime) / 500F);
            }

            //System.out.println(lastMiss + ", " + lastMissTime + ", " + start + ", " + currentComposite);

            if (currentComposite >= 0 && currentComposite <= 1) {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, currentComposite));
            } else {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0));
            }
        }
        g2d.setFont(nanum);
        //g.setColor(type.getColor());
        g2d.drawString(comboToStr, startX, startY);

        g2d.setTransform(originalAffine);
        g2d.setComposite(originalComposite);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }

    public void drawCombo(Graphics g, int combo, GameInformation information, long start, double percent) {

        Graphics2D g2d = (Graphics2D) g;

        String comboToStr = String.valueOf(combo);
        Point size = ImageUtility.getTextSize(nanum, comboToStr);
        int startX = (information.getWindowSize().getX() - size.getX()) / 2;
        int startY = (int) (information.getWindowSize().getY() / 4D * 1.2);

        Color originalColor = g2d.getColor();
        AffineTransform originalAffine = g2d.getTransform();
        Composite originalComposite = g2d.getComposite();
        Font originalFont = g2d.getFont();

        g2d.setColor(Color.WHITE);

        if (combo > 0) {
            lastMiss = false;
            if (percent < 0.3) {
                float currentPercentY = (float) (1 + percent * 1.6);
                float calculatedPercentY = currentPercentY;

                float currentPercentX = (float) (1 + percent * 0.6);
                float calculatedPercentX = currentPercentX;

                AffineTransform transform = new AffineTransform();
                transform.scale(calculatedPercentX, calculatedPercentY); // 1.15

                g2d.setTransform(transform);

                startX = ImageUtility.getResizedScalePoint(startX, size.getX(), calculatedPercentX);
                startY = ImageUtility.getResizedScalePoint(startY, -size.getY()/2, calculatedPercentY);

                lastPercentY = currentPercentY;
            } else if (percent < 0.6) {
                float currentPercentY = (float) (1 + (0.6 - percent) * 1.6);
                float calculatedPercentY = currentPercentY;

                float currentPercentX = (float) (1 + (0.6 - percent) * 0.6);
                float calculatedPercentX = currentPercentX;

                AffineTransform transform = new AffineTransform();
                transform.scale(calculatedPercentX, calculatedPercentY);
                g2d.setTransform(transform);
                startX = ImageUtility.getResizedScalePoint(startX, size.getX(), calculatedPercentX);
                startY = ImageUtility.getResizedScalePoint(startY, -size.getY()/2, calculatedPercentY);

                lastPercentY = currentPercentY;
            } else {

            }
        } else {
            float currentComposite;
            if (!lastMiss) {
                lastMiss = true;
                lastMissTime = start;
            }
            if (lastMissTime == start) {
                currentComposite = (float) (1 - percent);
            } else {
                currentComposite = 1 - ((System.currentTimeMillis() - lastMissTime) / 500F);
            }

            //System.out.println(lastMiss + ", " + lastMissTime + ", " + start + ", " + currentComposite);

            if (currentComposite >= 0 && currentComposite <= 1) {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, currentComposite));
            } else {
                g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0));
            }
        }
        g2d.setFont(nanum);
        //g.setColor(type.getColor());
        g2d.drawString(comboToStr, startX, startY);

        g2d.setTransform(originalAffine);
        g2d.setComposite(originalComposite);
        g2d.setFont(originalFont);
        g2d.setColor(originalColor);
    }
}
