package com.lbt.yingx_lbt.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class SecurityImage {
    /**

     * @param securityCode

     * @return

     */
    public static BufferedImage createImage(String securityCode){

        int codeLength = securityCode.length();
        
        int fontSize = 15;

        int fontWidth = fontSize+1;

         


        int width = codeLength*fontWidth+6;

        int height = fontSize*2+1;


        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);

        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.LIGHT_GRAY);

        g.setFont(new Font("Arial", Font.BOLD, height-2));

        g.drawRect(0, 0, width-1, height-1);

        Random rand = new Random();

        g.setColor(Color.LIGHT_GRAY);

        for (int i = 0; i < codeLength*6; i++) {

            int x = rand.nextInt(width);

            int y = rand.nextInt(height);

            g.drawRect(x, y, 1, 1);

        }

        int codeY = height-10;

        g.setColor(new Color(19,148,246));

        g.setFont(new Font("Georgia", Font.BOLD, fontSize));
        for(int i=0;i<codeLength;i++){
        	double deg=new Random().nextDouble()*20;
        	g.rotate(Math.toRadians(deg), i*16+13,codeY-7.5);
            g.drawString(String.valueOf(securityCode.charAt(i)), i*16+5, codeY);
            g.rotate(Math.toRadians(-deg), i*16+13,codeY-7.5);
        }
       
        g.dispose();

        return image;

    }

     

}
