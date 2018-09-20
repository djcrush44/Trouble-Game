/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
/**
 *
 * @author danielchruscielski
 */
public class Trouble2 extends JFrame {

    /**
     * @param args the command line arguments
     */
    private Screen screen;
    private Image bg;
    private Image die1;
    private Image red;
    private Image blue;
    private Image yell;
    private Image green;
    private Animation a;
    private Board b;
    
    public static void main(String[] args) {
        DisplayMode dm = new DisplayMode(1024,1280,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        Trouble2 t = new Trouble2();
        t.run(dm);
    }
    
    //loads pictures and adds scenes
    public void loadPics(){
        bg = new ImageIcon("U:\\Java\\Trouble2\\Pics\\Trouble.png").getImage();
        die1 = new ImageIcon("U:\\Java\\Trouble2\\Pics\\Die1.png").getImage();
        Image die2 = new ImageIcon("U:\\Java\\Trouble2\\Pics\\Die2.png").getImage();
        red = new ImageIcon("U:\\Java\\Trouble2\\Pics\\RedPiece.png").getImage();
        blue = new ImageIcon("U:\\Java\\Trouble2\\Pics\\BluePiece.png").getImage();
        yell = new ImageIcon("U:\\Java\\Trouble2\\Pics\\YellowPiece.png").getImage();
        green = new ImageIcon("U:\\Java\\Trouble2\\Pics\\GreenPiece.png").getImage();
        a = new Animation();
        a.addScene(die1,250);
        a.addScene(die2,250);
    }
    public void run(DisplayMode dm)
    {
        setBackground(new Color(101,73,32));
        setForeground(Color.WHITE);
        setFont(new Font("Arial",Font.PLAIN,24));
        
        screen = new Screen();
        try{
            screen.setFullScreen(dm, this);
            loadPics();
            //movieLoop();
            Graphics g = screen.getFullScreenWindow().getGraphics();
            b = new Board();
            for(int c= 0;c<29;c++){
                g.drawImage(red,b.getX(c),b.getY(c),null);
                try{
                    Thread.sleep(1000);
                }catch(Exception ex){}
            }
        }finally{
            screen.restoreScreen();
        }
    }
    
    //main movie loop
    public void movieLoop(){
        long startTime = System.currentTimeMillis();
        long cumTime = startTime;
        
        while(cumTime-startTime < 5000){
            long timePassed = System.currentTimeMillis()-cumTime;
            cumTime+=timePassed;
            a.update(timePassed);
            
            Graphics g = screen.getFullScreenWindow().getGraphics();
            draw(g);
            g.dispose();
            
            try{
                Thread.sleep(20);
            }catch(Exception ex){}
            
        }
    }
    
    public void draw(Graphics g){
        b = new Board();
        g.drawImage(bg, 0, 0, null);
        g.drawImage(die1, 500, 500, null);
        g.drawImage(red, b.getX(5), b.getY(5), null);
        g.drawImage(blue, b.getX(10), b.getY(10), null);
        g.drawImage(yell, b.getX(15), b.getY(15), null);
        g.drawImage(green, b.getX(20), b.getY(20), null);
        
    }
    
    @Override public void paint(Graphics g){
        if(g instanceof Graphics2D){
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        g = screen.getFullScreenWindow().getGraphics();
        Board b = new Board();
        g.drawImage(bg, 0, 0, null);
        g.drawImage(die1, 500, 500, null);
        g.drawImage(red, b.getX(5), b.getY(5), null);
        g.drawImage(blue, b.getX(10), b.getY(10), null);
        g.drawImage(yell, b.getX(15), b.getY(15), null);
        g.drawImage(green, b.getX(20), b.getY(20), null);
    }
}
