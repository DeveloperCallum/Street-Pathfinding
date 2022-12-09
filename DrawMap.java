// ******************************************************
// Do not change this code. We will make changes to this
// file for testing your submissions. Hence, if you rely
// on changes that you have made yourself, your  program
// might fail some of our tests.
// ******************************************************

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;
import java.lang.Thread;

public class DrawMap extends JFrame {
  private Graphics display;    // Graphics object needed to draw
  private Board panel;         // Panel containing map

  private int numNodes;        // Number of nodes in the graph
  private int mapWidth;        // Width of the map (number of nodes across)
  private int mapLength;       // Length of the map (number of nodes in a
                               // column of the graph)

  private int gridSize;        // Size of each square in the grid 
                               // representation of the graph
  private int start;           // Starting node 
  private int destination;     // Ending node
  private int numPrivate, numConstruction;

  private char[][] lab;
  private BufferedImage[] car = new BufferedImage[4];
  private BufferedImage[] imgs; // Image files used to display hosue blocks
  private BufferedImage construction;
  private int numImages = 10;
  private boolean imageFiles = true;
  private int lx = -1, ly, lwidth, lheight; // Last edge of the path

  private Color pathColor = new Color(0,0,255);
  private Color entranceColor = new Color(100,100,160);
  private Color exitColor = new Color(0,200,0);
  private Color backColor = new Color(140,200,160);
  private Color streetColor = new Color(160,160,160);
  private Color tollColor = new Color(220,120,80);
  private Color compColor = new Color(150,20,200);
  private Color white = new Color(255,255,255);
  private Color black = new Color(0,0,0);
  private Color brown2 = new Color(188,120,120);  
  private Color brown = new Color(168,100,100);
  private Color toll = new Color(255,0,155);
  private Color comp = new Color(255,255,15);
  
  private final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 0;
   
  /* ============================= */
  public DrawMap(String mapFile) {
  /* ============================= */
    BufferedReader input;

    try {
      numNodes = 0;
      panel = new Board();
      getContentPane().add(panel);
 
      input = new BufferedReader(new FileReader(mapFile));

      // Process first lines of the input file
      gridSize = Integer.parseInt(input.readLine());
      start = Integer.parseInt(input.readLine());
      destination = Integer.parseInt(input.readLine());
      mapWidth = Integer.parseInt(input.readLine());
      mapLength = Integer.parseInt(input.readLine());
      numPrivate = Integer.parseInt(input.readLine());
      numConstruction = -Integer.parseInt(input.readLine());


      numNodes = mapWidth * mapLength;
      
      if (mapLength > 15) setSize((2*mapWidth-1)*gridSize+10,(2*mapLength+2)*gridSize+12);
      else if (mapLength >= 10) setSize((2*mapWidth-1)*gridSize+10,(2*mapLength+1)*gridSize+12);
      else setSize((2*mapWidth-1)*gridSize+10,(2*mapLength)*gridSize+12);

      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setVisible(true);
      display = panel.getGraphics();

      Thread clock = new Thread();
      clock.sleep(2000);
      display.setColor(backColor);
      display.fillRect(0,0,(2*mapWidth-1)*gridSize,(2*mapLength-1)*gridSize);
      display.setColor(backColor);
      display.drawRect(0,0,(2*mapWidth-1)*gridSize,(2*mapLength-1)*gridSize);
      display.drawRect(1,1,(2*mapWidth-1)*gridSize-2,(2*mapLength-1)*gridSize-2);
      display.drawRect(2,2,(2*mapWidth-1)*gridSize-4,(2*mapLength-1)*gridSize-4);
      display.drawRect(3,3,(2*mapWidth-1)*gridSize-6,(2*mapLength-1)*gridSize-6);

      readImages();
      drawMyMap(input);

      display.setColor(Color.black);
      display.drawString("Private = "+numPrivate+", Construction = "+(-numConstruction),3,(2*mapLength-1)*gridSize+11);
      display.setColor(backColor);

    }
    catch (InterruptedException e) {
      System.out.println("Error starting program");
    }
    catch (Exception e) {
      System.out.println("Error opening file "+mapFile);
      mapWidth = 1;
      mapLength = 1;
      gridSize = 1;
    }
  }


 /* ================================================================= */
  private void readImages() {
  /* ================================================================= */
      imgs = new BufferedImage[numImages];

      try {
		for (int i = 1; i <= numImages; ++i) 
	             imgs[i-1] = ImageIO.read(new File("house"+i+".jpg"));
		car[RIGHT] = ImageIO.read(new File("car.jpg"));
		car[LEFT] = ImageIO.read(new File("carl.jpg"));
		car[DOWN] = ImageIO.read(new File("card.jpg"));
		car[UP] = ImageIO.read(new File("caru.jpg"));
		construction = ImageIO.read(new File("construction.jpg"));
      } catch (IOException e) {
	  imageFiles = false;
      }
  }

 /* ================================================================= */
  public void drawMyMap(BufferedReader input) {
  /* ================================================================= */
     int  i, row = 0;
     String line;

     try {
       lab = new char[2*mapLength-1][2*mapWidth-1];
       // Read the road map from the file
       for (;;) {
           line = input.readLine();
           if (line == null) {             // End of file
               input.close();
               break;
           }
           for (i = 0; i < line.length(); ++i) 
	       lab[row][i] = line.charAt(i);
           ++row;
       }
     }
     catch (Exception e) {
        System.out.println("Error reading input file "+e.getMessage());
        return;
     }

     for (row = 0; row < (2*mapLength-1); ++row)
           for (i = 0; i < (2*mapWidth-1); ++i)
           /* Draw a row of the map */
	         switch (lab[row][i]) {
		 case 'V': if ((row % 2) == 0) drawHorPay(i,row,false,tollColor,toll);
		           else drawVertPay(i,row,false,tollColor,toll);
                   break;
		 case 'C': if ((row % 2) == 0) drawHorPay(i,row,false,compColor,comp);
		           else drawVertPay(i,row,false,compColor,comp);
                   break;			   
          case 'B': drawBlock(i,row); break;
		  case 'P': if ((row % 2) == 0) drawHorRoad(i,row,streetColor);
			        else  drawVerRoad(i,row,streetColor); 
		            break;
		  case '+': drawIntersection(i,row); 
                 }

     drawStart(2*(start % mapWidth),2*(start/mapWidth),true);
     drawExit(2*(destination % mapWidth),2*(destination/mapWidth));
   }


  /* Draw a vertical private or construction road */
  /* ================================================================= */
  private void drawVertPay(int x, int y, boolean open, Color street, Color gate) {
  /* ================================================================= */
      int basex, basey;

      basex = x*gridSize;
      basey = y*gridSize;
      drawVerRoad(x,y,street);
      if (street == compColor) 
          display.drawImage(construction,x*gridSize+gridSize/5,
                  y*gridSize+gridSize/3,x*gridSize+4*gridSize/5,y*gridSize+2*gridSize/3,1,1,						
                  construction.getWidth(),construction.getHeight(),null);
      else {
      	if (open) openVPay(basex+gridSize/5,basey+2*gridSize/5,gate);
      	else drawVPay(basex+gridSize/5,basey+2*gridSize/5,gate);
      }
  }

  /* Draw a horizontal private or construction road */
  /* ================================================================= */
  private void drawHorPay(int x, int y, boolean open, Color street, Color gate) {
  /* ================================================================= */
      int basex, basey;

      basex = x*gridSize;
      basey = y*gridSize;
      drawHorRoad(x,y,street);
      if (street == compColor) 
          display.drawImage(construction,x*gridSize+gridSize/5,
                  y*gridSize+gridSize/3,x*gridSize+4*gridSize/5,y*gridSize+2*gridSize/3,1,1,						
                  construction.getWidth(),construction.getHeight(),null);
      else {
         if (open) openHPay(basex+2*gridSize/5,basey+gridSize/5,gate);
         else drawHPay(basex+2*gridSize/5,basey+gridSize/5,gate);
      }
  }
  
  /* Draw a vertical private or construction symbol */
  /* ================================================================= */
  private void drawVPay(int x, int y, Color gate) {
  /* ================================================================= */
      display.setColor(gate);
      display.fillRect(x,y,gridSize/5,gridSize/5);
      for (int i = -1; i < 2; ++i) {
          dashedLine(x+gridSize/5,y+gridSize/10+i,x+3*gridSize/5,
                     y+gridSize/10+i,Color.red);
          dashedLine(x+gridSize/5+5,y+gridSize/10+i,x+3*gridSize/5,
                     y+gridSize/10+i,Color.white);
      }
  }

  /* Draw a horizontal private or construction symbol */
  /* ================================================================= */
  private void drawHPay(int x, int y, Color gate) {
  /* ================================================================= */
      display.setColor(gate);
      display.fillRect(x,y,gridSize/5,gridSize/5);
      for (int i = -1; i < 2; ++i) {
          dashedLine(x+gridSize/10+i,y+gridSize/5,x+gridSize/10+i,
		     y+3*gridSize/5,Color.red);
          dashedLine(x+gridSize/10+i,y+gridSize/5+5,x+gridSize/10+i,
		     y+3*gridSize/5,Color.white);
      }
  }

  /* Draw a vertical open private or construction symbol */
  /* ================================================================= */
  private void openVPay(int x, int y, Color gate) {
  /* ================================================================= */
      display.setColor(gate);
      display.fillRect(x,y,gridSize/5,gridSize/5);
      for (int i = 0; i < 3; ++i) {
          dashedLine(x+gridSize/5+i,y+gridSize/10-2*gridSize/5,
   	             x+gridSize/5+i,y+gridSize/10,Color.red);
          dashedLine(x+gridSize/5+i,y+gridSize/10-2*gridSize/5+5,
		     x+gridSize/5+i,y+gridSize/10+5,Color.white);
      }
  }

  /* Draw a horizontal open private or construction symbol */
  /* ================================================================= */
  private void openHPay(int x, int y, Color gate) {
  /* ================================================================= */
      display.setColor(gate);
      display.fillRect(x,y,gridSize/5,gridSize/5);
      for (int i = 0; i < 3; ++i) {
          dashedLine(x+gridSize/10,y+gridSize/5+i,
   	             x+gridSize/10+2*gridSize/5,y+gridSize/5+i,Color.red);
          dashedLine(x+gridSize/10+5,y+gridSize/5+i,
		     x+gridSize/10+2*gridSize/5,y+gridSize/5+i,Color.white);
      }
  }

 /* ================================================================= */
  private void dashedLine(int x1, int y1, int x2, int y2, Color c) {
  /* ================================================================= */
      int dashSize = 5;
      int i;

      display.setColor(c);
      if (gridSize <= 2*dashSize) display.drawLine(x1,y1,x2,y2);
      else {
	 i = 0; 
         while (((y1 == y2) && ((x1+i) < x2)) ||
		((x1 == x2) && ((y1+i) < y2))) {

             if (y1 == y2) // Horizontal line  
		 if (x1+i+dashSize < x2)
	            display.drawLine(x1+i,y1,x1+i+dashSize,y2);
                 else display.drawLine(x1+i,y1,x2,y2);
             else 
                 if (y1+i+dashSize < y2)
		    display.drawLine(x1,y1+i,x2,y1+i+dashSize);
	         else display.drawLine(x1,y1+i,x2,y2);
             i = i + 2*dashSize;
	 }
      }
  }


 /* Draw a horizontal road */
 /* ================================================================= */
    private void drawHorRoad(int x, int y, Color c) {
  /* ================================================================= */
      display.setColor(c);
      display.fillRect(x*gridSize-gridSize/5,y*gridSize+gridSize/5,
			gridSize+2*gridSize/5,gridSize-2*gridSize/5);
      dashedLine(x*gridSize-gridSize/5,y*gridSize+gridSize/2,
		       x*gridSize+gridSize+gridSize/5,y*gridSize+gridSize/2,white);
  }


 /* Draw a vertical road */
 /* ================================================================= */
    private void drawVerRoad(int x, int y, Color c) {
  /* ================================================================= */
      display.setColor(c);
      display.fillRect(x*gridSize+gridSize/5,y*gridSize-gridSize/5,
		       gridSize-2*gridSize/5,gridSize+2*gridSize/5);
      dashedLine(x*gridSize+gridSize/2,y*gridSize-gridSize/5,
		       x*gridSize+gridSize/2,y*gridSize+gridSize+gridSize/5,white);
  }

  /* ================================================================= */
  private void drawCar(int x, int y, int dir) {
  /* ================================================================= */
      if (imageFiles)
		  if (dir == RIGHT)
		    display.drawImage(car[RIGHT],x*gridSize+gridSize/5,y*gridSize+gridSize/3,x*gridSize+4*gridSize/5,y*gridSize+2*gridSize/3,1,1,
							  car[RIGHT].getWidth(),car[RIGHT].getHeight(),null);
		  else if (dir == LEFT)
		    display.drawImage(car[LEFT],x*gridSize+gridSize/5,y*gridSize+gridSize/3,x*gridSize+4*gridSize/5,y*gridSize+2*gridSize/3,1,1,
							  car[LEFT].getWidth(),car[LEFT].getHeight(),null);
		  else if (dir == UP)
		    display.drawImage(car[UP],x*gridSize+gridSize/3,y*gridSize+gridSize/5,x*gridSize+2*gridSize/3,y*gridSize+4*gridSize/5,1,1,
							  car[UP].getWidth(),car[UP].getHeight(),null);
		  else 
		    display.drawImage(car[DOWN],x*gridSize+gridSize/3,y*gridSize+gridSize/5,x*gridSize+2*gridSize/3,y*gridSize+4*gridSize/5,1,1,
							  car[DOWN].getWidth(),car[DOWN].getHeight(),null);
	  else drawCar1(x,y);

  }
  
 /* ================================================================= */
  private void drawBlock(int x, int y) {
  /* ================================================================= */
      boolean t = true;
      int i;
      if (imageFiles) {
			i = (int)(Math.floor(Math.random() * numImages)); 
			display.drawImage(imgs[i],x*gridSize,y*gridSize,x*gridSize+gridSize,y*gridSize+gridSize,1,1,
							  imgs[i].getWidth(),imgs[i].getHeight(),null);
      }
      else {
		display.setColor(backColor);
		display.fillRect(x*gridSize,y*gridSize,gridSize,gridSize);
      }
  }


/* ================================================================= */
  private void drawIntersection(int x, int y) {
  /* ================================================================= */
      int x1, x2, y1, y2;
      int basex = x * gridSize;
      int basey = y * gridSize;

      display.setColor(streetColor);
      display.fillRect(x*gridSize+gridSize/5,y*gridSize+gridSize/5,
           	       gridSize-2*gridSize/5,gridSize-2*gridSize/5);

      if ((x > 0) && (lab[y][x-1] != ' ')) x1 = basex+gridSize/5;
      else x1 = basex+gridSize/2;
      if ((x < (2*mapWidth-2)) && (lab[y][x+1] != ' ')) x2 = basex+4*gridSize/5;
      else x2 = basex+gridSize/2;
      if ((y > 0) && (lab[y-1][x] != ' ')) y1 = basey+gridSize/5;
      else y1 = basey+gridSize/2;
      if ((y < (2*mapLength-2)) && (lab[y+1][x] != ' ')) y2 = basey+4*gridSize/5;
      else y2 = basey+gridSize/2;

      dashedLine(x1,basey+gridSize/2,x2,basey+gridSize/2,white);
      dashedLine(basex+gridSize/2,y1,basex+gridSize/2,y2,white);
  }

  /* ================================================================= */
  private void drawCar1(int x, int y) {
  /* ================================================================= */
      int basey = y*gridSize+3*gridSize/8;
      int basex = x*gridSize+gridSize/4;

      display.setColor(Color.red);
      display.fillRoundRect(basex,basey,gridSize/2,gridSize/4,3,3);
      display.setColor(Color.gray);
      display.fillRoundRect(basex+gridSize/4,basey+1,gridSize/8,gridSize/4-2,2,2);
      display.setColor(brown);
      display.fillRoundRect(basex+gridSize/8,basey+1,gridSize/8,gridSize/4-2,1,1);
      display.setColor(brown2);
      display.fillRoundRect(basex+gridSize/8+1,basey+2,gridSize/8-1,gridSize/8-3,1,1);
      display.fillRoundRect(basex+gridSize/8+1,basey+gridSize/8+1,gridSize/8-1,gridSize/8-3,1,1);

      display.setColor(Color.yellow);
      display.fillRoundRect(basex+gridSize/2-3,basey+1,3,gridSize/8-2,2,2);
      display.fillRoundRect(basex+gridSize/2-3,basey+gridSize/8+1,3,gridSize/8-2,2,2);
  }

  /* Draw the starting point */
  /* ================================================================= */
  private void drawStart(int x, int y, boolean car) {
  /* ================================================================= */
    int basex = x*gridSize;
    int basey = y*gridSize;

    drawIntersection(x,y);

    display.setColor(entranceColor);
    display.fillRect(basex+gridSize/5,basey+gridSize/5,
		     3*gridSize/5,3*gridSize/5);

    for (int i = 0; i < 4; ++i)
       dashedLine(basex+3*gridSize/5+i,basey+gridSize/5,basex+3*gridSize/5+i,
		  basey+4*gridSize/5,Color.yellow);

    if (car) drawCar(x,y,RIGHT);

  }

  /* Draw the destination */
  /* ================================================================= */
  private void drawExit(int x, int y) {
  /* ================================================================= */
    int basex = x*gridSize;
    int basey = y*gridSize;
    int square = 5, r = 0, c;

    drawIntersection(x,y);
    display.setColor(white);
    display.fillRect(basex+gridSize/4,basey+gridSize/4,gridSize/2,gridSize/4);
    display.setColor(black);
    display.drawLine(basex+gridSize/4,basey+gridSize/4,basex+gridSize/4,
                     basey+3*gridSize/4);

    basex = basex+gridSize/4;
    basey = basey+gridSize/4;

    if (gridSize >= 10) {
      while (r < (gridSize/4)) {
          if (((r / square) % 2) == 0) c = 0;
          else c = square;
	  while (c < (gridSize/2)) {
              if ((r+square) < (gridSize/4))
                  if ((c+square) < (gridSize/2))
	            display.fillRect(basex+c,basey+r,square,square);
                  else display.fillRect(basex+c,basey+r,(gridSize/2)-c,square);
              else if ((c+square) < (gridSize/2))
		    display.fillRect(basex+c,basey+r,square,(gridSize/4)-r);
                  else display.fillRect(basex+c,basey+r,(gridSize/2)-c,
					(gridSize/4)-r);
              c = c + 2*square;
	  }
          r = r+square;
      }
    }
  }

  /* Draws an edge of the solution */
  /* ========================================================== */
  private void drawEdge(int u1, int v1) {
  /* ========================================================== */
  /* Input: edge (uv) */
   
     int x, y, width, height, offset, labx, laby, row, i, row2, i2;
     int u, v;

     if (u1 > numNodes || v1 > numNodes) {
        System.out.println("Invalid edge ("+u1+","+v1+")");
        return;
     }
     offset = gridSize / 2 - 1;
     if (u1 > v1) {u = v1; v = u1;} else {u = u1; v = v1;}

     x = (u % mapWidth)*2*gridSize + offset;
     y = (u / mapWidth)*2*gridSize + offset;

     if (v == (u+1)) { 
       width = gridSize*2;
       height = 3;
       labx = 2*(u % mapWidth)+1;
       laby = 2*(u / mapWidth);
       row = laby;
       row2 = row;
       if (u1 <= v1) {i = labx - 1; i2 = i + 2;} else {i2 = labx - 1; i = i2 + 2;}
       if (lab[laby][labx] == 'T') 
          drawHorPay((u % mapWidth)*2+1,(u / mapWidth)*2,true,tollColor,toll);
	   else if (lab[laby][labx] == 'C') 
          drawHorPay((u % mapWidth)*2+1,(u / mapWidth)*2,true,compColor,comp);
     }
     else if (v == (u+mapWidth)) {
       width = 3;
       height = gridSize*2;
       labx = 2*(u % mapWidth);
       laby = 2*(u / mapWidth)+1;
       row = laby - 1;
       i = labx;
       if (u1 <= v1) {row = laby - 1; row2 = row + 2;} else {row2 = laby - 1; row = row2 + 2;};
       i2 = i;
       if (lab[laby][labx] == 'T') 
           drawVertPay((u % mapWidth)*2,(u / mapWidth)*2+1,true,tollColor,toll);
	   else if (lab[laby][labx] == 'C') 
           drawVertPay((u % mapWidth)*2,(u / mapWidth)*2+1,true,compColor,comp);
     }
     else {
        System.out.println("Invalid edge ("+u+","+v+")");
        return;
     }

	 if (((row/2)*mapWidth + i/2) == start) drawStart (i,row,false);
	 else if (((row/2)*mapWidth + i/2) == destination) drawExit(i,row);
	 else if (lab[row][i] == 'X') drawBlock(i,row);
	 else if (lab[row][i] == '+') drawIntersection(i,row);

     if (lx != -1) { // Re-draw the last edge, as it was deleted by the car
        display.setColor(pathColor);
        display.fillRect(lx,ly,lwidth,lheight);
     }

     display.setColor(pathColor);
     display.fillRect(x,y,width,height);

	if (v1 == (u1 + 1)) drawCar(i2,row2,RIGHT);  
	else if (v1 == (u1 - 1)) drawCar(i2,row2,LEFT);
	else if (v1 > u1) drawCar(i2,row2,DOWN);
	else drawCar(i2,row2,UP);

     lx = x; ly = y; lwidth = width; lheight = height;
  }


  /* Draws an edge connecting the given nodes */
  /* ==================================== */
  public void drawEdge (Node u, Node v) {
  /* ==================================== */
      if (u != v) drawEdge(u.getId(),v.getId());
  }
}
