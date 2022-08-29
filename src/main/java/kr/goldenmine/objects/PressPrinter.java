package kr.goldenmine.objects;

import kr.goldenmine.GameInformation;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ehe12 on 2018-11-14.
 */
public class PressPrinter {
    private static final List<AlphaComposite> compositeModels = new ArrayList<>();

    static {
        for(int i = 0; i < 200; i++) {
            compositeModels.add(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)i/200));
        }
    }

    public void onPress(Graphics g, HashMap<Integer, Boolean> state, GameInformation info) {
        Graphics2D g2d = (Graphics2D)g;

        int width = info.getNoteSize().getX();
        int height = (int) (info.getWindowSize().getY()/1.5);
        int startY = info.getPerfectY()-height;

        double divide10 = height/100;


        for(int line : state.keySet()) {
            boolean clicked = state.get(line);

            if(clicked) {
                //System.out.println(startY + ", " + info.getPerfectY() + ", " + height + ", " + divide10);

                int startX = info.getRealCenterXPosition(line+1);
                int XSize = (int) (info.getNoteSize().getX()*1.2);


                Composite originalComposite = g2d.getComposite();
                Color originColor = g2d.getColor();

                g2d.setColor(Color.WHITE);

                int index = 0;

                for(double i = startY+divide10; i <= info.getPerfectY(); i+=divide10) {
                    AlphaComposite composite = compositeModels.get(index);
                    g2d.setComposite(composite);

                    g2d.fillRect(startX-XSize/2,(int) (i-divide10), XSize, (int) divide10);

                    index++;
                }
                //System.out.println(startX-XSize/2 + ", " + startY+ ", " + XSize+ ", " + height);
                //g2d.fillRect(startX-XSize/2, startY, XSize, height);

                g2d.setColor(originColor);
                g2d.setComposite(originalComposite);
            }
        }
    }
}
