/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trouble2;

/**
 *
 * @author danielchruscielski
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] go = {774, 694, 594, 495, 390, 295, 208, 135, 81, 70, 70, 70, 85, 136, 207, 292, 394, 495, 600, 695, 783, 853, 907, 920, 919, 921, 907, 855,
        780, 835, 888, 926, 56, 97, 150, 200, 55, 99, 148, 198, 782, 838, 890, 930,
        781, 730, 679, 626, 200, 253, 305, 355, 210, 254, 305, 356, 778, 731, 680, 630};
        int[] goY = {836, 893, 903, 900, 903, 891, 837, 764, 677, 578, 473, 372, 270, 188, 113, 57, 44, 41, 41, 45, 113, 186, 270, 371, 476, 578, 681, 764,
        915, 875, 830, 778, 772, 833, 884, 923, 183, 129, 79, 34, 37, 81, 130, 185,
        768, 717, 662, 615, 770, 712, 665, 613, 187, 240, 294, 344, 190, 239, 285, 340};
        String out = "{";
        for(int c = 0;c<go.length;c++){
            go[c] = (int)(go[c]*0.830078125 - 30); // 30 for y
            go[c] = go[c]+3;
            out += Integer.toString(go[c]);
            out += (c==go.length-1)?"}":",";
            if(c%10==9 && c!= go.length-1)
                out+="\n";
        }
        out+=";";
        System.out.println(out);
    }
}
