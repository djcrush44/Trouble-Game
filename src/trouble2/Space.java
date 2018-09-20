/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

/**
 *
 * @author danielchruscielski
 */
public class Space {
    
    private int x;
    private int y;
    private int player;
    
    public Space(){
        x = 0;
        y = 0;
        player = -1;
    }
    public Space(int x, int y)
    {
        this.x = x;
        this.y = y;
        player = -1;
    }
    public Space(int x, int y,int p)
    {
        this.x = x;
        this.y = y;
        player = p;
    }
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public boolean isFilled(){
        return player>=0 && player<4;
            
    }
    public boolean isEmpty(){
        return (player==-1);
    }
    
}
