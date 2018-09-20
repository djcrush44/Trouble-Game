/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 *
 * @author danielchruscielski
 */
public class Test extends JFrame {

    /**
     * @param args the command line arguments
     */
    private Board b;
    private Die d;
    private Graphics dbg;
    private Image dbImage;
    private int currentPlayer;
    private boolean rolled;
    private boolean canMove;
    private boolean finishedRoll;
    private boolean[] goalFull;
    private int[] startRolls;
    private int[] winOrder;
    private int winSpot;
    private int max;
    private int rollAnimation;
    private int height;
    private int width;
    private int center;
    private int left;
    private int right;
    private Clip push, pop;
    //private AudioInputStream audioStream;

    public class MouseInput implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (state == STATE.START) {

                if (y >= 300 && y <= 500) {
                    if (x >= left && x <= left + 200) {
                        b = new Board(2);
                        goalFull = new boolean[2];
                        startRolls = new int[2];
                        winOrder = new int[2];
                    } else if (x >= center && x <= center + 200) {
                        b = new Board(3);
                        goalFull = new boolean[3];
                        startRolls = new int[3];
                        winOrder = new int[3];
                    } else if (x >= right && x <= right + 200) {
                        b = new Board(4);
                        goalFull = new boolean[4];
                        startRolls = new int[4];
                        winOrder = new int[4];
                    }
                    b.setX(width / 2 - b.getWidth() / 2);
                    b.setY((height - b.getHeight()) / 2 + 25);

                    state = STATE.CPU;
                }
                if (x >= center && x <= center + 200 && y >= height - 150 && y <= height - 50) {
                    state = STATE.RULES;
                }
            } else if (state == STATE.CPU) {
                if (y >= height / 2 - 200 && y <= height / 2 + 100) {
                    if (x >= 100 && x <= 400) {
                        if (currentPlayer == b.getNumPlayers() - 1) {
                            state = STATE.PICKFIRST;
                            cyclePlayer();
                            if (b.getPlayer(0).isCPU()) {
                                quickPop();
                                fieldRoll();
                            }
                        }
                        else
                            cyclePlayer();

                    } else if (x >= width - 400 && x <= width - 100) {
                        b.getPlayer(currentPlayer).setCPU(true);
                        if (currentPlayer == b.getNumPlayers() - 1) {
                            state = STATE.PICKFIRST;
                            cyclePlayer();
                            if (b.getPlayer(0).isCPU()) {
                                quickPop();
                                fieldRoll();
                            }
                        } else {
                            cyclePlayer();
                        }
                    }
                }
            }
            else if(state==STATE.RULES){
                state = STATE.START;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (state == STATE.GAME && !rolled || state == STATE.PICKFIRST) {
                if (y >= 320 + b.getY() && y <= 487 + b.getY()) {
                    if (x >= 320 + b.getX() && x <= 487 + b.getX()) {
                        try {
                            pop.stop();
                            push.setFramePosition(0);
                            push.loop(0);
                        } catch (Exception ex) {
                            System.out.println("not playing");
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (state == STATE.PICKFIRST) {
                if (y >= 320 + b.getY() && y <= 487 + b.getY()) {
                    if (x >= 320 + b.getX() && x <= 487 + b.getX()) {
                        try{
                            push.stop();
                            pop.setFramePosition(0);
                            pop.loop(0);
                        }catch (Exception ex){}
                        
                        fieldRoll();
                    }
                }
            } 
            else if (state == STATE.CONFIRM) {
                rolled = b.getPlayer(currentPlayer).isCPU();
                if (b.getPlayer(currentPlayer).isCPU()) {
                    quickPop();
                    d.setRoll();
                }
                state = STATE.GAME;
                rollAnimation = 0;

            }
            if (state == STATE.GAME && !b.getPlayer(currentPlayer).isCPU()) {
                if ((!canMove || goalFull[currentPlayer]) && (d.getRoll() != 6 && rolled)) {
                    cyclePlayer();
                    if (goalFull[currentPlayer]) {
                        cyclePlayer();
                    }
                    if (b.getPlayer(currentPlayer).isCPU()) {
                        d.setRoll();
                        rolled = true;
                        quickPop();
                    } else {
                        rolled = false;
                    }
                    canMove = true;
                } else if (y >= 320 + b.getY() && y <= 487 + b.getY() && !rolled) {
                    if (x >= 320 + b.getX() && x <= 487 + b.getX()) {
                        d.setRoll();
                        rolled = true;
                        try{
                            pop.setFramePosition(0);
                            pop.loop(0);
                        }catch (Exception ex){}
                    }
                    if (!(b.canMove(b.getPlayer(currentPlayer).getSpace(0), d.getRoll(), currentPlayer)
                            || b.canMove(b.getPlayer(currentPlayer).getSpace(1), d.getRoll(), currentPlayer)
                            || b.canMove(b.getPlayer(currentPlayer).getSpace(2), d.getRoll(), currentPlayer)
                            || b.canMove(b.getPlayer(currentPlayer).getSpace(3), d.getRoll(), currentPlayer))) {
                        canMove = false;
                    }
                } else if (rolled) {
                    for (int c = 0; c < 4; c++) {
                        int space = b.getPlayer(currentPlayer).getSpace(c);
                        int bx = b.getX(space);
                        int by = b.getY(space);
                        if (x >= bx && x <= bx + 40 && y >= by && y <= by + 60) {
                            if (b.movePiece(space, d.getRoll(), currentPlayer)) {
                                if (b.goalFull(currentPlayer)) {
                                    goalFull[currentPlayer] = true;
                                    winOrder[winSpot++] = currentPlayer;
                                    cyclePlayer();
                                    if (goalFull[currentPlayer]) {
                                        cyclePlayer();
                                    }
                                    if (b.getPlayer(currentPlayer).isCPU()) {
                                        d.setRoll();
                                        rolled = true;
                                        quickPop();
                                    }
                                    int count = 0;
                                    for (int d = 0; d < goalFull.length; d++) {
                                        if (goalFull[d]) {
                                            count++;
                                        }
                                    }
                                    if (count >= b.getNumPlayers() - 1) {
                                        for (int d = 0; d < b.getNumPlayers(); d++) {
                                            if (!goalFull[d]) {
                                                winOrder[winOrder.length - 1] = d;
                                            }
                                        }
                                        if(!b.getPlayer(currentPlayer).isCPU())
                                            quickPop();
                                        state = STATE.END;
                                    }
                                } else if (d.getRoll() != 6) {
                                    cyclePlayer();
                                    if (goalFull[currentPlayer]) {
                                        cyclePlayer();
                                    }
                                    if (b.getPlayer(currentPlayer).isCPU()) {
                                        d.setRoll();
                                        rolled = true;
                                        quickPop();
                                    }
                                }
                                if (!b.getPlayer(currentPlayer).isCPU()) {
                                    rolled = false;
                                }
                                canMove = true;
                                break;
                            }
                        }
                    }
                    if (d.getRoll() == 6 && !canMove) {
                        rolled = false;
                        canMove = true;
                    }

                }

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private enum STATE {

        START, CPU, PICKFIRST, RULES, CONFIRM, GAME, END
    };
    private STATE state;

    private void setUp() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        height = getHeight();
        width = getWidth();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        addMouseListener(new MouseInput());
        this.getContentPane().setBackground(new Color(101, 73, 60));
        setFont(new Font("Arial", Font.PLAIN, 24));

        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(new File("Sounds\\push.wav"));
            AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(new File("Sounds\\pop.wav"));
            AudioFormat format = audioStream.getFormat();
            AudioFormat format2 = audioStream2.getFormat();
            

            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(Clip.class,
                    format); // format is an AudioFormat object
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Not Supported");
            }
            // Obtain and open the line.
            try {
                push = (Clip)AudioSystem.getLine(info);
                push.open(audioStream);
                info = new DataLine.Info(Clip.class,
                    format2);
                pop = (Clip)AudioSystem.getLine(info);
                pop.open(audioStream2);
                push.setLoopPoints(50, -1);
                pop.setLoopPoints(0, -1);
            } catch (LineUnavailableException ex) {
                // Handle the error.
                //... 
            }
            
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void quickPop(){
        try{
            Thread.sleep(100);
        }catch(Exception ex){}
        push.setFramePosition(0);
        push.start();
        try{
            Thread.sleep(100);
        }catch(Exception ex){}
        pop.setFramePosition(0);
        pop.start();
        
    }

    private void fieldRoll() {
        
        rollAnimation = 0;
        d.setRoll();
        rolled = true;
        startRolls[currentPlayer] = d.getRoll();
        if (currentPlayer < b.getNumPlayers() - 1) {
            currentPlayer++;
        } else {
            max = 0;
            for (int c = b.getNumPlayers() - 1; c >= 0; c--) {
                if (startRolls[c] >= startRolls[max]) {
                    max = c;
                }
            }
            currentPlayer = max;
            state = STATE.CONFIRM;
        }
    }

    private void moveCPU() {

        int[] spaces = new int[4];
        for (int d = 0; d < 4; d++) {
            spaces[d] = b.getPlayer(currentPlayer).getSpace(d);
        }
        int optimal = 0;
        int start = b.getPlayer(currentPlayer).getStartSpace();
        int goal = b.getPlayer(currentPlayer).getGoalStart();
        int home = b.getPlayer(currentPlayer).getHomeStart();
        boolean[][] decision = new boolean[5][4];
        int[] goalDist = new int[4];
        for (int s = 0; s < 4; s++) {
            if (b.canMove(spaces[s], d.getRoll(), currentPlayer)) {
                decision[4][s] = true;
                if (spaces[s] >= home && spaces[s] < home + 4 && d.getRoll() == 6) {
                    decision[0][s] = true;
                }
                if (b.willEnterGoal(spaces[s], d.getRoll(), currentPlayer)) {
                    decision[1][s] = true;
                }
                if (b.willTroublePlayer(spaces[s], d.getRoll(), currentPlayer)) {
                    decision[2][s] = true;
                }
                for (int a = 0; a < b.getNumPlayers(); a++) {
                    if (spaces[s] == b.getPlayer(a).getStartSpace()) {
                        decision[3][s] = true;
                        break;
                    }
                }
            }
        }
        for (int s = 0; s < 5; s++) {
            if (decision[s][0] || decision[s][1] || decision[s][2] || decision[s][3]) {
                for (int d = 0; d < 4; d++) {
                    if (decision[s][d]) {
                        optimal = d;
                        break;
                    }
                }
                if (decision[s][optimal]) {
                    break;
                }

            }
        }
        b.movePiece(b.getPlayer(currentPlayer).getSpace(optimal), d.getRoll(), currentPlayer);
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }
        if (b.goalFull(currentPlayer)) {
            goalFull[currentPlayer] = true;
            winOrder[winSpot++] = currentPlayer;
            cyclePlayer();
            if (goalFull[currentPlayer]) {
                cyclePlayer();
            }

            int count = 0;
            for (int d = 0; d < goalFull.length; d++) {
                if (goalFull[d]) {
                    count++;
                }
            }
            if (count >= b.getNumPlayers() - 1) {
                for (int d = 0; d < b.getNumPlayers(); d++) {
                    if (!goalFull[d]) {
                        winOrder[winOrder.length - 1] = d;
                    }
                }
                state = STATE.END;
            } else if (b.getPlayer(currentPlayer).isCPU()) {
                d.setRoll();
                rolled = true;
            }
        }
        if (d.getRoll() == 6 && !goalFull[currentPlayer]) {
            d.setRoll();
            rollAnimation = 0;
            finishedRoll = false;
            quickPop();
        } else {
            cyclePlayer();
            while (goalFull[currentPlayer]) {
                cyclePlayer();
            }
            if (b.getPlayer(currentPlayer).isCPU()) {
                rolled = true;
                d.setRoll();
                quickPop();
            }
        }

    }

    public Test() {
        super("Trouble");
        setUp();

        state = STATE.START;

        d = new Die();
        currentPlayer = 0;
        rolled = false;
        canMove = true;
        finishedRoll = false;
        winSpot = 0;
        rollAnimation = 0;

    }

    @Override
    public void paint(Graphics g) {

        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        if (state == STATE.START) {
            inputPlayers(g);
            repaint();
        } else if (state == STATE.CPU) {
            chooseCPU(g);
            repaint();
        } else if (state == STATE.PICKFIRST) {
            drawBoard(g);
            showStartRolls(g);
            playerMessage(g);
            if (rollAnimation == 11 && b.getPlayer(currentPlayer).isCPU()) {
                quickPop();
                fieldRoll();
            }
            repaint();
        } else if (state == STATE.CONFIRM) {
            drawBoard(g);
            showStartRolls(g);
            playerMessage(g);
            repaint();
        } else if (state == STATE.RULES) {
            //inputPlayers(g);
            rules(g);
            repaint();
        } else if (state == STATE.GAME) {
            drawBoard(g);
            if (finishedRoll && b.getPlayer(currentPlayer).isCPU()) {
                moveCPU();
            }
            repaint();
        } else if (state == STATE.END) {
            endScreen(g);
        }
    }

    private void showStartRolls(Graphics g) {
        int nx = 0;
        int ny = 0;
        g.setColor(Color.WHITE);
        g.setFont(new Font(("Arial"), Font.BOLD, 64));
        for (int c = 0; c < b.getNumPlayers(); c++) {
            if (startRolls[c] != 0) {
                switch (c) {
                    case 0:
                        nx = b.getX() + b.getWidth() + 50;
                        ny = b.getY() + b.getHeight();
                        break;
                    case 1:
                        nx = b.getX() - 50;
                        ny = b.getY() + b.getHeight();
                        break;
                    case 2:
                        nx = b.getX() - 50;
                        ny = b.getY() + 50;
                        break;
                    case 3:
                        nx = b.getX() + b.getWidth() + 50;
                        ny = b.getY() + 50;
                        break;
                }
                g.drawString(Integer.toString(startRolls[c]), nx, ny);
            }
        }
    }

    private void inputPlayers(Graphics g) {
        setBackground(new Color(0, 100, 200));
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("How Many Players?", width / 2 - 150, 100);
        g.setColor(Color.RED);
        center = width / 2 - 100;
        left = center - 400;
        right = center + 400;
        g.fillRect(left, 300, 200, 200);
        g.setColor(Color.BLUE);
        g.fillRect(center, 300, 200, 200);
        g.setColor(Color.YELLOW);
        g.fillRect(right, 300, 200, 200);
        g.setColor(Color.WHITE);
        g.fillRect(center, height - 150, 200, 100);
        g.setColor(Color.BLACK);
        g.drawRect(center, height - 150, 200, 100);
        g.drawString("Rules", center + 50, height - 100);
        g.drawString("2", left + 100, 400);
        g.drawString("3", center + 100, 400);
        g.drawString("4", right + 100, 400);

    }

    private void chooseCPU(Graphics g) {
        setBackground(new Color(0, 100, 200));
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int num = currentPlayer + 1;
        String title = "Player " + num + ": Human or CPU?";
        g.drawString(title, width / 2 - 100, 100);
        g.setColor(Color.RED);
        g.fillRect(100, height / 2 - 200, 300, 300);
        g.setColor(Color.BLUE);
        g.fillRect(width - 400, height / 2 - 200, 300, 300);
        g.setColor(Color.BLACK);
        g.drawString("Human", 200, height / 2 - 100);
        g.drawString("CPU", width - 300, height / 2 - 100);

    }

    private void endScreen(Graphics g) {
        switch (winOrder[0]) {
            case 0:
                setBackground(Color.RED);
                break;
            case 1:
                setBackground(Color.BLUE);
                break;
            case 2:
                setBackground(Color.YELLOW);
                break;
            case 3:
                setBackground(Color.GREEN);
                break;
            default:
                setBackground(new Color(101, 73, 60));
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 64));
        String out = "Game Over";
        g.drawString(out,width/2-200,height/6);
        for (int d = 0; d < b.getNumPlayers(); d++) {
            out = "";
            switch (winOrder[d]) {
                case 0:
                    out = "Red";
                    break;
                case 1:
                    out = "Blue";
                    break;
                case 2:
                    out = "Yellow";
                    break;
                case 3:
                    out = "Green";
                    break;
                default:
                    out = "Red";
            }
            switch (d) {
                case 0:
                    out += " ... 1st!";
                    break;
                case 1:
                    out += " ... 2nd";
                    break;
                case 2:
                    out += " ... 3rd";
                    break;
                case 3:
                    out += " ... 4th";
                    break;
                default:
                    out += " ERROR";
            }
            g.drawString(out, width / 2 - 200, height/6 * (d+2));
        }
    }

    private void drawBoard(Graphics g) {

        switch (currentPlayer) {
            case 0:
                setBackground(Color.RED);
                g.setColor(Color.RED);
                break;
            case 1:
                setBackground(Color.BLUE);
                g.setColor(Color.BLUE);
                break;
            case 2:
                setBackground(Color.YELLOW);
                g.setColor(Color.YELLOW);
                break;
            case 3:
                setBackground(Color.GREEN);
                g.setColor(Color.GREEN);
                break;
            default:
                setBackground(new Color(101, 73, 60));

        }
        g.drawImage(b.getImage(), b.getX(), b.getY(), null);

        for (int c = 0; c < b.getNumPlayers(); c++) {
            for (int f = 0; f < 4; f++) {
                int check = b.getPlayer(c).getSpace(f);
                g.drawImage(b.getPlayer(c).getPiece(), b.getX(check), b.getY(check), null);
            }
        }
        if (!canMove && rolled && rollAnimation == 11) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.fillRect(b.getX() + 275, b.getY() + 150, 250, 60);
            g.setColor(Color.BLACK);
            g.drawString("No Possible Moves", b.getX() + 300, b.getY() + 175);
            if (d.getRoll() != 6) {
                g.drawString("Click to continue", b.getX() + 310, b.getY() + 200);
            } else {
                g.drawString("Roll again", b.getX() + 310, b.getY() + 200);
            }
        }
        if (rollAnimation < 11 && rolled) {
            d.setX();
            d.setY();
            rollAnimation++;
        } else if (rolled) {
            finishedRoll = true;
        } else {
            rollAnimation = 0;
        }
        g.drawImage((rollAnimation == 0 || rollAnimation == 11) ? d.getImage() : d.getRandomImage(), d.getX() + b.getX(), d.getY() + b.getY(), null);
        if(rollAnimation==11){
            pop.stop();
        }
    }

    private void playerMessage(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.fillRect(b.getX() + 100, b.getY() + 200, 600, 75);
        g.setColor(Color.BLACK);
        g.drawRect(b.getX() + 100, b.getY() + 200, 600, 75);
        if (state == state.PICKFIRST) {
            g.drawString("Each player should roll once to determine who goes first.", b.getX() + 130, b.getY() + 250);
        } else {
            String out = "";
            switch (max) {
                case 0:
                    out = "Red";
                    break;
                case 1:
                    out = "Blue";
                    break;
                case 2:
                    out = "Yellow";
                    break;
                case 3:
                    out = "Green";
                    break;
                default:
                    out = "Red";
            }
            g.drawString(out + " will go first!", b.getX() + 350, b.getY() + 250);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("(Click to begin the game)", b.getX() + 550, b.getY() + 270);
        }

    }

    private void rules(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.fillRect(width / 4, 200, 650, 500);
        g.setColor(Color.BLACK);
        g.drawRect(width / 4, 200, 650, 500);
        int loc = width / 4 + 25;
        g.drawString("Each player should click the bubble to pop the die and decide who goes first.", loc, 250);
        g.drawString("On your turn, pop once for a chance to move.", loc, 300);
        g.drawString("If it's a six, you can click on one piece to move it out of the home row.", loc, 350);
        g.drawString("Otherwise, you can only select and move pieces not in the home row.", loc, 400);
        g.drawString("Anytime you pop a six, you get to pop and move again!", loc, 450);
        g.drawString("You can not move your piece to a spot one of your pieces already occupies.", loc, 500);
        g.drawString("If you land on another player, you can send them back to the home row. Trouble!", loc, 550);
        g.drawString("First to get all pieces in their goal wins. Keep playing against who's left!", loc, 600);
        g.drawString("Click to go back", loc, 675);
    }

    private void cyclePlayer() {
        if (currentPlayer == b.getNumPlayers() - 1) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
        rollAnimation = 0;
        rolled = false;
        canMove = true;
        finishedRoll = false;
    }

    public static void main(String[] args) {
        Test go = new Test();
    }
}
