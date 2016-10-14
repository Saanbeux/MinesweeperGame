import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
    private Random generator = new Random();
    public boolean[][] mineArray = new boolean[10][10];
    public int [][] preparedSpace = new int [10][10];
    public boolean [][] sweepedSpace = new boolean [10][10];
    public boolean gameStart = true;
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
        case 1:        //Left mouse button
            Component c = e.getComponent();
            while (!(c instanceof JFrame)) {
                c = c.getParent();
                if (c == null) {
                    return;
                }
            }
            JFrame myFrame = (JFrame) c;
            MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
            Insets myInsets = myFrame.getInsets();
            int x1 = myInsets.left;
            int y1 = myInsets.top;
            e.translatePoint(-x1, -y1);
            int x = e.getX();
            int y = e.getY();
            myPanel.x = x;
            myPanel.y = y;
            myPanel.mouseDownGridX = myPanel.getGridX(x, y);
            myPanel.mouseDownGridY = myPanel.getGridY(x, y);
            myPanel.repaint();

            break;
        case 3:        //Right mouse button
            c = e.getComponent();
            while (!(c instanceof JFrame)) {
                c = c.getParent();
                if (c == null) {
                    return;
                }
            }
            myFrame = (JFrame) c;
            myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
            myInsets = myFrame.getInsets();
            x1 = myInsets.left;
            y1 = myInsets.top;
            e.translatePoint(-x1, -y1);
            x = e.getX();
            y = e.getY();
            myPanel.x = x;
            myPanel.y = y;
            myPanel.mouseDownGridX = myPanel.getGridX(x, y);
            myPanel.mouseDownGridY = myPanel.getGridY(x, y);
            myPanel.repaint();

            break;
        default:    //Some other button (2 = Middle mouse button, etc.)
            //Do nothing
            break;
        }
    }
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
        case 1: //Left mouse button
            Component c = e.getComponent();
            while (!(c instanceof JFrame)) {
                c = c.getParent();
                if (c == null) {
                    return;
                }
            }
            JFrame myFrame = (JFrame)c;
            MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
            Insets myInsets = myFrame.getInsets();
            int x1 = myInsets.left;
            int y1 = myInsets.top;
            e.translatePoint(-x1, -y1);
            int x = e.getX();
            int y = e.getY();
            myPanel.x = x;
            myPanel.y = y;
            int gridX = myPanel.getGridX(x, y);
            int gridY = myPanel.getGridY(x, y);

            if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
                //Had pressed outside
                //Do nothing
            } else {
                if ((gridX == -1) || (gridY == -1)) {
                    //Is releasing outside
                    //Do nothing
                } else {
                    if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
                        //Released the mouse button on a different cell where it was pressed
                        //Do nothing
                    } else {
                        //Released the mouse button on the same cell where it was pressed
                        if ((gridX == 0) && (gridY == 0)||gameStart) {
                            gameStart = false;
                            for (int i = 1; i < 10; i++) {   //The rest of the grid
                                for (int j = 1; j < 10; j++) {
                                    mineArray[i][j] = false;
                                    preparedSpace[i][j]= 0;
                                    sweepedSpace [i][j]= false;
                                    myPanel.colorArray[i][j] = Color.WHITE;
                                }
                            }
                            myPanel.repaint();
                            randomBombs();
                            for (int i = 1; i < 10; i++) {   //The rest of the grid
                                for (int j = 1; j < 10; j++) {
                                    sweep(i,j);
                                }}
                            //start game
                        }else{
                            if ((gridX == 0) || (gridY == 0)) {
                                //On the left column and on the top row... do nothing]
                            }else{
                                if (mineArray[gridX][gridY]){
                                    for (int i=1;i<10;i++){
                                        for (int j=1;j<10;j++){
                                            if(mineArray[i][j]){
                                                myPanel.colorArray[i][j] = Color.BLACK;
                                            }}}
                                    myPanel.repaint();
                                    return;
                                }else {
                                    if (!sweepedSpace[gridX][gridY]){
                                        reveal(gridX,gridY);
                                        for (int i=1;i<10;i++){
                                            for (int j=1;j<10;j++){
                                                if (sweepedSpace[i][j] && !mineArray[i][j]){
                                                    myPanel.colorArray[i][j] = setColors(i,j);
                                                }else{
                                                    // do nothing
                                                }
                                            }
                                        }
                                    }
                                }
                                myPanel.repaint();
                            }}}}
            }
            break;
        case 3:        //Right mouse button
            c = e.getComponent();
            while (!(c instanceof JFrame)) {
                c = c.getParent();
                if (c == null) {
                    return;
                }
            }
            myFrame = (JFrame)c;
            myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
            myInsets = myFrame.getInsets();
            x1 = myInsets.left;
            y1 = myInsets.top;
            e.translatePoint(-x1, -y1);
            x = e.getX();
            y = e.getY();
            myPanel.x = x;
            myPanel.y = y;
            gridX = myPanel.getGridX(x, y);
            gridY = myPanel.getGridY(x, y);
            Color flagColor = Color.RED;
            if ((gridX == 0) || (gridY == 0)) {
                //On the left column and on the top row... do nothing
            }else{
                if (!sweepedSpace[gridX][gridY] || mineArray[gridX][gridY]){
                    Color cellColor = myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY];
                    if (cellColor.equals(flagColor)){
                        myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.WHITE;
                    }else{
                        myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = flagColor;
                        myPanel.repaint();
                    }
                }
            }
            myPanel.repaint();
            break;
        default:    //Some other button (2 = Middle mouse button, etc.)
            //Do nothing
            break;
        }

    }

    public void randomBombs(){
        int row, column;
        for (int i=0;i<10;i++){
            row = generator.nextInt(9)+1;
            column = generator.nextInt(9)+1;
            mineArray[row][column] = true;
        }
    }
    public Color setColors(int i, int j){
        int colorID = preparedSpace[i][j];
        Color newColor = null;
        switch (colorID){
        case 0:
            newColor = Color.lightGray;
            break;
        case 1:
            newColor = Color.cyan;
            break;
        case 2:
            newColor = Color.YELLOW;
            break;
        case 3:
            newColor = Color.GREEN;
            break;
        case 4:
            newColor = new Color(0x964B00);  //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
            break;
        case 5:
            newColor = new Color(0xB57EDC); //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
            break;
        case 6:
            newColor = Color.BLUE;
            break;
        case 7:
            newColor = Color.MAGENTA;
            break;
        case 8:
            newColor = Color.DARK_GRAY;
            break;
        case 9:
            newColor = Color.WHITE;
            break;
        }
        return newColor;
    }
    public void subReveal(int i,int j){
        if ((!sweepedSpace[i][j]) && (preparedSpace[i][j] == 0)){
            sweepedSpace[i][j] =true;
            reveal(i,j);
        }else{
            sweepedSpace[i][j] =true;
        }}
    public void reveal(int gridX, int gridY){
        if (!sweepedSpace[gridX][gridY] && preparedSpace[gridX][gridY]!=0){
            subReveal(gridX,gridY);
        }
                    else{
                      
                                if (gridX==9 && gridY==9){
                                    for (int i=gridX-1; i<gridX+1; i++){
                                        for (int j = gridY-1; j<gridY+1; j++){
                                            subReveal(i,j);}}} 
                                else{
                              
                                	if (gridX==1 && gridY==9){
                                        for (int i= gridX-1; i<gridX+2; i++){
                                            for (int j = gridY-1; j<gridY+1; j++){
                                                subReveal(i,j);
                                            }}}
                                    else{
                                        if (gridX==9 && gridY==1){
                                            for (int i= gridX-1; i<gridX+1; i++){
                                                for (int j = gridY-1; j<gridY+2; j++){
                                                    subReveal(i,j);
                                                }}}
                                        else{
                                            if (gridX==9 && gridY!=9 && gridY!=0){
                                                for (int i= gridX-1; i<gridX+1; i++){
                                                    for (int j = gridY-1; j<gridY+2; j++){
                                                        subReveal(i,j);
                                                    }}}
                                            else{
                                                if (gridX!=9 && gridY==9 && gridX!=0){
                                                    for (int i= gridX-1; i<gridX+2; i++){
                                                        for (int j = gridY-1; j<gridY+1; j++){

                                                            subReveal(i,j);
                                                        }}}
                             
                                        else{
                                            if(gridX>0&&gridX<9 && gridY>0&&gridY<9){
                                                for (int i= gridX-1; i<gridX+2; i++){
                                                    for (int j = gridY-1; j<gridY+2; j++){
                                                        subReveal(i,j);
                                                    }}}
                                            else{
                                                if (sweepedSpace[gridX][gridY]){
                                                    return;
                                                }else{
                                                }
                                            }}}}}}}}

    public int sweep(int x, int y){
        int counter=0;
        if (x==9 && y!=9){
            for (int i= x-1; i<x+1; i++){
                for (int j = y-1; j<y+2; j++){
                    if (mineArray[i][j]){
                        counter++;
                    }
                }
            }
        }else{
            if (x!=9 && y==9){
                for (int i= x-1; i<x+2; i++){
                    for (int j = y-1; j<y+1; j++){
                        if (mineArray[i][j]){
                            counter++;
                        }
                    }
                }
            }else{
                if (x==9 && y==9){
                    for (int i= x-1; i<x+1; i++){
                        for (int j = y-1; j<y+1; j++){
                            if (mineArray[i][j]){
                                counter++;
                            }}}}
                else{
                    if (x==9 && y==1){
                        for (int i= x-1; i<x+1; i++){
                            for (int j = y-1; j<y+2; j++){
                                if (mineArray[i][j]){
                                    counter++;
                                }}}}
                    else{
                        if (x==1 && y==9){

                            for (int i= x-1; i<x+2; i++){
                                for (int j = y-1; j<y+1; j++){
                                    if (mineArray[i][j]){
                                        counter++;
                                    }}}
                        }else{
                            for (int i= x-1; i<x+2; i++){
                                for (int j = y-1; j<y+2; j++){
                                    if (mineArray[i][j]){
                                        counter++;
                                    }}}}}}}}return preparedSpace[x][y] = counter;}

}