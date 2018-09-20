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
public class Images extends JFrame {

    private Screen s;
    private Image bg;
    private Image red;
    private Image blue;
    private Image green;
    private Image yel;
    private boolean loaded;
    private Die d;
    
    public static void main(String[] args) {
        DisplayMode dm = new DisplayMode(1024,1280,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        Images i = new Images();
        i.run(dm);
    }
    
    
    public void run(DisplayMode dm)
    {
        setBackground(new Color(101,73,32));
        setForeground(Color.WHITE);
        setFont(new Font("Arial",Font.PLAIN,24));
        loaded = false;
        
        s = new Screen();
        try{
            s.setFullScreen(dm, this);
            loadPics();
            try{
                Thread.sleep(5000);
            }catch (Exception ex){System.out.println("HELP");}
        }finally{
            s.restoreScreen();
        }
    }
    
    //loads pictures
    public void loadPics(){
        bg = new ImageIcon("U:\\Java\\Trouble2\\Pics\\trouble.png").getImage();
        red = new ImageIcon("U:\\Java\\Trouble2\\Pics\\RedPiece.png").getImage();
        blue = new ImageIcon("U:\\Java\\Trouble2\\Pics\\BluePiece.png").getImage();
        green = new ImageIcon("U:\\Java\\Trouble2\\Pics\\GreenPiece.png").getImage();
        yel = new ImageIcon("U:\\Java\\Trouble2\\Pics\\YellowPiece.png").getImage();
        d = new Die();
        loaded = true;
        repaint();
    }
    
    @Override public void paint(Graphics g){
        if(g instanceof Graphics2D){
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if(loaded){
            g.drawImage(bg,100,0,null);
            g.drawImage(red,200,100,null);
            g.drawImage(blue,250,100,null);
            g.drawImage(yel,300,100,null);
            g.drawImage(green,350,100,null);
            d.setRoll();
            g.drawImage(d.getImage(),600,450,null);
        }
    }
}
