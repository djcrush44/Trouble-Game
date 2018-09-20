/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author danielchruscielski
 */
public class Player extends Sprite {

    private int num;
    private int startSpace;
    private int homeStart;
    private int goalStart;
    private int[] space;
    private Image piece;
    private boolean isCPU;

    public Player() {
        num = 0;
        startSpace = 0;
        homeStart = 0;
        goalStart = 0;
        space = new int[4];
        piece = null;
        isCPU = false;
    }

    public Player(int p) {
        num = p;
        space = new int[4];
        isCPU = false;
        switch (num) {
            case 0:
                startSpace = 0;
                homeStart = 28;
                goalStart = 44;
                piece = new ImageIcon("Pics\\TestRedPiece.png").getImage();
                break;
            case 1:
                startSpace = 7;
                homeStart = 32;
                goalStart = 48;
                piece = new ImageIcon("Pics\\TestBluePiece.png").getImage();
                break;
            case 2:
                startSpace = 14;
                homeStart = 36;
                goalStart = 52;
                piece = new ImageIcon("Pics\\TestYellowPiece.png").getImage();
                break;
            case 3:
                startSpace = 21;
                homeStart = 40;
                goalStart = 56;
                piece = new ImageIcon("Pics\\TestGreenPiece.png").getImage();
                break;
            default:
                startSpace = 0;
                homeStart = 28;
                goalStart = 44;
                piece = new ImageIcon("Pics\\RedPiece.png").getImage();
                break;
        }
        for (int c = 0; c < 4; c++) {
            space[c] = homeStart + c;
        }
    }
    public Player(int p, boolean CPU) {
        num = p;
        space = new int[4];
        isCPU = CPU;
        switch (num) {
            case 0:
                startSpace = 0;
                homeStart = 28;
                goalStart = 44;
                piece = new ImageIcon("U:\\Pics\\TestRedPiece.png").getImage();
                break;
            case 1:
                startSpace = 7;
                homeStart = 32;
                goalStart = 48;
                piece = new ImageIcon("U:\\Pics\\TestBluePiece.png").getImage();
                break;
            case 2:
                startSpace = 14;
                homeStart = 36;
                goalStart = 52;
                piece = new ImageIcon("U:\\Pics\\TestYellowPiece.png").getImage();
                break;
            case 3:
                startSpace = 21;
                homeStart = 40;
                goalStart = 56;
                piece = new ImageIcon("U:\\Pics\\TestGreenPiece.png").getImage();
                break;
            default:
                startSpace = 0;
                homeStart = 28;
                goalStart = 44;
                piece = new ImageIcon("U:\\Pics\\RedPiece.png").getImage();
                break;
        }
        for (int c = 0; c < 4; c++) {
            space[c] = homeStart + c;
        }
    }

    public int getNum() {
        return num;
    }

    public int getStartSpace() {
        return startSpace;
    }

    public int getGoalStart() {
        return goalStart;
    }

    public int getHomeStart() {
        return homeStart;
    }

    public int getSpace(int num) {
        return space[num];
    }
    public boolean isCPU(){
        return isCPU;
    }
    public void setCPU(boolean is){
        isCPU = is;
    }

    public void setSpace(int pieceNum, int spot) {
        space[pieceNum] = spot;
    }

    public int getPieceNum(int pos) {
        for (int c = 0; c < 4; c++) {
            if (space[c] == pos) {
                return c;
            }
        }
        return -1;
    }

    public Image getPiece() {
        return piece;
    }
    @Override public boolean equals(Object o)
    {
        Player obj = (Player)o;
        if(getNum()==obj.getNum())
        {
            return true;
        }
        return false;
    }
}
