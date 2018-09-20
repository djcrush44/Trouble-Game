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
public class Die extends Sprite{
    private Image die1;
    private Image die2;
    private Image die3;
    private Image die4;
    private Image die5;
    private Image die6;
    private int roll;
    
    public Die(){
        die1 = new ImageIcon("Pics\\Die1.png").getImage();
        die2 = new ImageIcon("Pics\\Die2.png").getImage();
        die3 = new ImageIcon("Pics\\Die3.png").getImage();
        die4 = new ImageIcon("Pics\\Die4.png").getImage();
        die5 = new ImageIcon("Pics\\Die5.png").getImage();
        die6 = new ImageIcon("Pics\\Die6.png").getImage();
        roll = (int)(Math.random()*6)+1;
        x = 360;
        y = 354;
    }
    
    public int getRoll(){
        return roll;
    }
    public void setX()
    {
        x = (int)(335+(Math.random()*80));
    }
    public void setY()
    {
        y = (int)(335+(Math.random()*80));
    }
    public Image getImage(){
        Image value = null;
        switch(roll){
            case 1 : value = die1; break;
            case 2 : value = die2; break;
            case 3 : value = die3; break;
            case 4 : value = die4; break;
            case 5 : value = die5; break;
            case 6 : value = die6; break;
            default : value = die6;
        }
        return value;
    }
    public Image getRandomImage(){
        Image value = null;
        switch((int)(Math.random()*6+1)){
            case 1 : value = die1; break;
            case 2 : value = die2; break;
            case 3 : value = die3; break;
            case 4 : value = die4; break;
            case 5 : value = die5; break;
            case 6 : value = die6; break;
            default : value = die6;
        }
        return value;
    }
    public void setRoll()
    {
        roll = (int)(Math.random()*6)+1;
    }
}
