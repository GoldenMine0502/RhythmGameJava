package com.GoldenMine;

import java.awt.Color;

/**
 * Created by ehe12 on 2018-11-12.
 */
public enum Judgement {
    PERFECT(2, Color.BLUE, 50), GREAT(5, Color.GREEN, 40), GOOD(10, Color.ORANGE, 25), BAD(20, Color.LIGHT_GRAY, 10), NONE(Integer.MAX_VALUE-1, Color.BLACK, 0), MISS(Integer.MAX_VALUE, Color.GRAY, 0);

    int judgement;
    Color color;

    int score;

    Judgement(int judgement, Color color, int score) {
        this.judgement = judgement;
        this.color = color;
        this.score = score;
    }

    public int getJudgement() {
        return judgement;
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }
}
