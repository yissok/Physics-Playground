package sample;

import javax.swing.*;
import java.util.Random;

public class Controller
{




    public static void main(String[] args) {
        // set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        int size=60;

        Controller c=new Controller();
        int[][] energyGrid= c.generateGrid(size);
        // create the example JFrame



         UsingGraphics2D window = new UsingGraphics2D(energyGrid);
         window.setVisible(true);
         window.start();

    }

    public int[][] generateGrid(int size)
    {
        int newSize=0;
        if (size%2==0)
        {
            newSize=size+1;
        }
        else
        {
            newSize=size+2;
        }
        int[][] energyGrid = new int[newSize][newSize];
        for (int i=0;i<newSize;i++)
        {
            for (int j=0;j<newSize;j++)
            {
                Random rand = new Random();
                energyGrid[i][j] = rand.nextInt(40);
            }
        }
        return energyGrid;
    }


}
