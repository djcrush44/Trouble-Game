/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

import java.awt.Image;

/**
 *
 * @author danielchruscielski
 */
public class Sprite {
    
    protected Image img;
    protected int x;
    protected int y;
    
    public Sprite(){
        x = 500;
        y = 500;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Image getImage(){
        return img;
    }
    public int getWidth(){
        return img.getHeight(null);
    }
    public int getHeight(){
        return img.getHeight(null);
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
