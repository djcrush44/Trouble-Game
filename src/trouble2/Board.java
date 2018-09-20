/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author danielchruscielski
 */
public class Board extends Sprite {

    private Space[] main;
    private ArrayList<Player> list;
    private final int[] xlist = {615, 549, 466, 383, 296, 217, 145, 85, 40, 31,
        31, 31, 43, 85, 144, 215, 300, 383, 467, 549,
        622, 681, 725, 736, 735, 737, 725, 682, 620, 666,
        710, 741, 19, 53, 97, 139, 18, 55, 95, 137,
        622, 668, 711, 744, 621, 578, 536, 492, 139, 183,
        226, 267, 147, 183, 226, 268, 618, 579, 537, 495};
    private final int[] ylist = {663, 711, 719, 717, 719, 709, 664, 604, 531, 449,
        362, 278, 194, 126, 63, 17, 6, 4, 4, 15,
        63, 124, 194, 277, 365, 449, 535, 604, 729, 696,
        658, 615, 610, 661, 703, 736, 121, 77, 35, -1,
        0, 37, 77, 123, 607, 565, 519, 480, 609, 561,
        522, 478, 125, 169, 214, 255, 127, 168, 206, 252};

    public Board() {
        super();
        x = 100;
        y = 100;
        img = new ImageIcon("Pics\\TroubleBoard.png").getImage();

        main = new Space[60];
        for (int c = 0; c < 60; c++) {
            main[c] = new Space(xlist[c], ylist[c]);
        }

        list = new ArrayList<>();
        for (int c = 0; c < 4; c++) {
            list.add(new Player(c));
            for (int d = 0; d < 4; d++) {
                setSpace(list.get(c).getHomeStart() + d, c);
            }
        }
    }

    public Board(int numPlayers) {
        super();
        x = 100;
        y = 50;
        img = new ImageIcon("Pics\\TroubleBoard.png").getImage();
        main = new Space[60];
        for (int c = 0; c < 60; c++) {
            main[c] = new Space(xlist[c], ylist[c]);
        }
        list = new ArrayList<>();
        for (int c = 0; c < numPlayers; c++) {

            list.add(new Player(c));
            for (int d = 0; d < 4; d++) {
                setSpace(list.get(c).getHomeStart() + d, c);
            }
        }
    }

    public int getX(int space) {
        return x + main[space].getX();
    }

    public int getY(int space) {
        return y + main[space].getY();
    }

    public int getSpace(int space) {
        return main[space].getPlayer();
    }

    private void setSpace(int space, int player) {
        main[space].setPlayer(player);
    }

    public boolean movePiece(int start, int roll, int player) {
        int end = (start + roll < 28) ? start + roll : start + roll - 28; // wrap around for main play area
        int pieceNum = list.get(player).getPieceNum(start);
        if (main[start].isFilled()) {
            if (main[list.get(player).getStartSpace()].getPlayer() != player
                    && start >= list.get(player).getHomeStart()
                    && start < (list.get(player).getHomeStart() + 4)
                    && roll == 6) {
                int startSpace = list.get(player).getStartSpace();
                if (main[startSpace].getPlayer() != -1) {
                    putHome(main[startSpace].getPlayer(), list.get(main[startSpace].getPlayer()).getPieceNum(startSpace));
                }
                list.get(player).setSpace(list.get(player).getPieceNum(start), startSpace);
                main[startSpace].setPlayer(player);
                main[start].setPlayer(-1);
                return true;
            }
            if (player != 0 && start < list.get(player).getStartSpace() && end >= list.get(player).getStartSpace()) {
                int spot = end - list.get(player).getStartSpace();
                int finish = list.get(player).getGoalStart() + spot;
                if (finish <= list.get(player).getGoalStart() + 3 && main[finish].isEmpty()) {
                    setSpace(finish, player);    //set space on board
                    setSpace(start, -1);
                    list.get(player).setSpace(pieceNum, finish);  //set piece location on player list
                    return true;
                }
                return false;
            } else if (player == 0 && end < start && start < list.get(player).getHomeStart()) {

                if (end <= 3 && main[list.get(player).getGoalStart() + end].isEmpty()) {
                    setSpace(list.get(player).getGoalStart() + end, player);
                    setSpace(start, -1);
                    list.get(player).setSpace(pieceNum, list.get(player).getGoalStart() + end);
                    return true;
                }
                return false;
            }
            if (start >= list.get(player).getGoalStart() && start + roll < list.get(player).getGoalStart() + 4 && main[start + roll].isEmpty()) {
                setSpace(start + roll, player);
                list.get(player).setSpace(pieceNum, start + roll);
                setSpace(start, -1);
                return true;
            }
            if (main[end].getPlayer() != player && start < list.get(player).getHomeStart()) {
                if (main[end].getPlayer() != -1) //send opponent back to start
                {
                    putHome(main[end].getPlayer(), list.get(main[end].getPlayer()).getPieceNum(end));
                }

                list.get(player).setSpace(pieceNum, end);
                main[end].setPlayer(player); // switch pieces
                main[start].setPlayer(-1);
                return true;
            }
        }
        return false;
    }

    public boolean canMove(int start, int roll, int player) {
        int end = (start + roll < 28) ? start + roll : start + roll - 28; // wrap around for main play area
        int pieceNum = list.get(player).getPieceNum(start);
        if (main[start].isFilled()) {
            if (main[list.get(player).getStartSpace()].getPlayer() != player
                    && start >= list.get(player).getHomeStart()
                    && start < (list.get(player).getHomeStart() + 4)
                    && roll == 6) {
                return true;
            } else if (player != 0 && start < list.get(player).getStartSpace() && end >= list.get(player).getStartSpace()) {
                int spot = end - list.get(player).getStartSpace();
                int finish = list.get(player).getGoalStart() + spot;
                if (finish <= list.get(player).getGoalStart() + 3 && main[finish].isEmpty()) {
                    return true;
                }
                return false;
            } else if (player == 0 && end < start && start < list.get(player).getHomeStart()) {
                if (end <= 3 && main[list.get(player).getGoalStart() + end].isEmpty()) {
                    return true;
                }
                return false;
            }
            if (start >= list.get(player).getGoalStart() && start + roll < list.get(player).getGoalStart() + 4 && main[start + roll].isEmpty()) {
                return true;
            }
            if (main[end].getPlayer() != player && start < list.get(player).getHomeStart()) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean willEnterGoal(int start, int roll, int player) {
        int end = (start + roll < 28) ? start + roll : start + roll - 28; // wrap around for main play area
        int pieceNum = list.get(player).getPieceNum(start);
        if (player != 0 && start < list.get(player).getStartSpace() && end >= list.get(player).getStartSpace()) {
            int spot = end - list.get(player).getStartSpace();
            int finish = list.get(player).getGoalStart() + spot;
            if (finish <= list.get(player).getGoalStart() + 3 && main[finish].isEmpty()) {
                return true;
            }
            return false;
        } else if (player == 0 && end < start && start < list.get(player).getHomeStart()) {
            if (end <= 3 && main[list.get(player).getGoalStart() + end].isEmpty()) {
                return true;
            }
            return false;
        }
        return false;

    }
    
    public boolean willTroublePlayer(int start, int roll, int player){
        int end = (start + roll < 28) ? start + roll : start + roll - 28; // wrap around for main play area
        int pieceNum = list.get(player).getPieceNum(start);
        if (main[start].isFilled() && start>=0 && start<28) {
            if(main[end].isFilled() && main[end].getPlayer() !=player)
                return true;
        }
        return false;
    }


    private boolean putHome(int player, int piece) {
        int homeStart = list.get(player).getHomeStart();
        for (int r = homeStart; r < homeStart + 4; r++) {
            if (main[r].isEmpty()) {
                main[r].setPlayer(player);
                list.get(player).setSpace(piece, r);
                return true;
            }
        }
        return false;
    }

    public boolean goalFull(int player) {
        int start = list.get(player).getGoalStart();
        for (int c = start; c < start + 4; c++) {
            if (main[c].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int getNumPlayers() {
        return list.size();
    }

    public Player getPlayer(int num) {
        return list.get(num);
    }

    public void removePlayer(int num) {
        Player test = new Player(num);
        list.remove(test);
    }
//    public int getNextPlayer(){
//        
//    }

    public String toString() {
        String out = "";
        for (Player each : list) {
            out += "" + each.getNum() + "\n";
        }
        return out;
    }
}
