/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *     and the following disclaimer in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or
 *     promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package sample;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

/**
 * Class used to show a simple example of using the dyn4j project using
 * Java2D for rendering.
 * <p>
 * This class can be used as a starting point for projects.
 * @author William Bittle
 * @version 3.2.0
 * @since 3.0.0
 */
public class UsingGraphics2D extends JFrame {
	/** The serial version id */
	private static final long serialVersionUID = 5663760293144882635L;

	/** The scale 45 pixels per meter */
	public static final double SCALE = 10.0;

	/** The conversion factor from nano to base */
	public static final double NANO_TO_BASE = 1.0e9;

	/**
	 * Custom Body class to add drawing functionality.
	 * @author William Bittle
	 * @version 3.0.2
	 * @since 3.0.0
	 */
	public static class GameObject extends Body
	{
		/** The color of the object */
		protected Color color;

		/**
		 * Default constructor.
		 */
		public GameObject() {
			// randomly generate the color
			this.color = new Color(
					(float)Math.random() * 0.5f + 0.5f,
					(float)Math.random() * 0.5f + 0.5f,
					(float)Math.random() * 0.5f + 0.5f);
		}

		/**
		 * Draws the body.
		 * <p>
		 * Only coded for polygons and circles.
		 * @param g the graphics object to render to
		 */
		public void render(Graphics2D g) {
			// save the original transform
			AffineTransform ot = g.getTransform();

			// transform the coordinate system from world coordinates to local coordinates
			AffineTransform lt = new AffineTransform();
			lt.translate(this.transform.getTranslationX() * SCALE, this.transform.getTranslationY() * SCALE);
			lt.rotate(this.transform.getRotation());

			// apply the transform
			g.transform(lt);

			// loop over all the body fixtures for this body
			for (BodyFixture fixture : this.fixtures) {
				// get the shape on the fixture
				Convex convex = fixture.getShape();
				Graphics2DRenderer.render(g, convex, SCALE, color);
			}

			// set the original transform
			g.setTransform(ot);
		}
	}

	/** The canvas to draw to */
	protected Canvas canvas;

	/** The dynamics engine */
	protected World world;

	/** Wether the example is stopped or not */
	protected boolean stopped;



	/** The time stamp for the last iteration */
	protected long last;

	protected int[][] energyGrid;
	protected boolean initalPaint;
	protected float globalScale;
	protected int muoversiSuEGiu;
	protected int muoversiDestraESinistra;
	protected boolean[] applyForceNESW=new boolean[4];
	protected Settings settings;

	public void printGrid(int[][] energyGrid)
	{
		for (int i=0;i<energyGrid.length;i++)
		{
			for (int j=0;j<energyGrid[0].length;j++)
			{
				System.out.print(energyGrid[i][j]+" ");
			}
			System.out.println();
		}
	}


	int mouseX;
	int mouseY;
	boolean mouseYes;
	/**
	 * Default constructor for the window
	 */
	public UsingGraphics2D(int[][] energyGrid) {
		super("Graphics2D Example");
		applyForceNESW[0]=false;
		applyForceNESW[1]=false;
		applyForceNESW[2]=false;
		applyForceNESW[3]=false;




		System.out.println("Simple GUI");
		settings = new Settings(this);
		settings.setVisible(true);
		settings.setBounds(850,100,350,300);
		System.out.println("Simple GUIIIII");


		muoversiSuEGiu=0;
		muoversiDestraESinistra=0;
		globalScale=(float) 0.4;
		initalPaint=false;
		this.energyGrid=energyGrid;
		// setup the JFrame
		printGrid(this.energyGrid);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add a window listener
		this.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				// before we stop the JVM stop the example
				stop();
				super.windowClosing(e);
			}
		});

		Dimension size = new Dimension(800, 600);
		this.canvas = new Canvas();
		this.canvas.setPreferredSize(size);
		this.canvas.setMinimumSize(size);
		this.canvas.setMaximumSize(size);
		mouseYes=false;
		this.canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x=e.getX()-155;
				int y=e.getY()-55;
//				System.out.println(x+","+y);//these co-ords are relative to the component
				mouseX=x;
				mouseY=y;
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		});
		this.canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x=e.getX()-155;
				int y=e.getY()-55;
//				System.out.println(x+","+y);//these co-ords are relative to the component
				mouseX=x;
				mouseY=y;
				mouseYes=true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseYes=false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
		this.add(this.canvas);
		this.setResizable(false);
		this.pack();
		this.stopped = false;
		this.initializeWorld();
	}




	private static class StopContactListener extends CollisionAdapter {
		private Body b1, b2;
		private String desc1, desc2;
		private String[] setOfIDs;
		public StopContactListener(Body b1, Body b2, String desc1, String desc2, String[] setOfIDs) {
			this.b1 = b1;
			this.b2 = b2;
			this.desc1 = desc1;
			this.desc2 = desc2;
			this.setOfIDs = setOfIDs;
		}
		public int findIndexOfID(String anID)
		{
			for (int i=0;i<setOfIDs.length;i++)
			{
				if (anID.equals(setOfIDs[i]))
				{
					return i;
				}
			}
			return -1;
		}
		@Override
		public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
			// the bodies can appear in either order
			//System.out.println("collision: "+body1.getId());
			//System.out.println("oo: "+body2.getId());
			int a=findIndexOfID(body1.getId().toString());
			int b=findIndexOfID(body2.getId().toString());
			if (a!=-1)
				if (b !=-1)
					System.out.println("Object "+a+" touched object "+b);


			/*if ((body1 == b1 && body2 == b2) ||
					(body1 == b2 && body2 == b1)) {
				// its the collision we were looking for
				// do whatever you need to do here

				// stopping them like this isn't really recommended
				// there are probably better ways to do what you want

				body1.getLinearVelocity().zero();
				body1.setAngularVelocity(0.0);
				body2.getLinearVelocity().zero();
				body2.setAngularVelocity(0.0);
				return false;
			}*/
			return true;
		}
	}



	/**
	 * Creates game objects and adds them to the world.
	 * <p>
	 * Basically the same shapes from the Shapes test in
	 * the TestBed.
	 */
	protected void initializeWorld() {
		// create the world
		this.world = new World();

		this.world.setGravity(World.ZERO_GRAVITY);
		// create all your bodies/joints

		// create the floor
		int bounds=120;
		Rectangle floorRect = new Rectangle(bounds, bounds);
		GameObject[] floor = new GameObject[4];
		for (int i=0;i<4;i++)
		{
			floor[i] = new GameObject();
			floor[i].addFixture(floorRect,1,0,1);
			floor[i].setMass(MassType.INFINITE);
		}

		floor[0].translate(0, bounds);
		floor[1].translate(-bounds, 0);
		floor[2].translate(0, -bounds);
		floor[3].translate(bounds, 0);
		this.world.addBody(floor[0]);
		this.world.addBody(floor[1]);
		this.world.addBody(floor[2]);
		this.world.addBody(floor[3]);


		// create a circle
		Circle[] cirShape = new Circle[10];
		GameObject[] circle = new GameObject[10];
		String[] setOfIDs = new String[10];


		int i=0;

		System.out.println("i: "+ i);
		cirShape[i]=new Circle(4);
		circle[i]=new GameObject();
		circle[i].addFixture(cirShape[i],1,11,0.2);//  0.126 oz/in^3 = 217.97925 kg/m^3
		circle[i].setMass(MassType.NORMAL);
		circle[i].translate((0), (6));
		circle[i].applyForce(new Vector2(400, 10));
		String a="hello"+i;
		circle[i].setUserData(a);
		// set some linear damping to simulate rolling friction
		circle[i].setLinearDamping(0.5);
		//circle[i].setLinearVelocity(0.1, -2);
		this.world.addBody(circle[i]);
		System.out.println("i: "+ circle[i].getId());
		setOfIDs[i]=circle[i].getId().toString();

		this.world.addListener(new StopContactListener(circle[1],circle[0],"a","b",setOfIDs));

	}

	/**
	 * Start active rendering the example.
	 * <p>
	 * This should be called after the JFrame has been shown.
	 */
	public void start() {
		// initialize the last update time
		this.last = System.nanoTime();
		// don't allow AWT to paint the canvas since we are
		this.canvas.setIgnoreRepaint(true);
		// enable double buffering (the JFrame has to be
		// visible before this can be done)
		this.canvas.createBufferStrategy(2);
		// run a separate thread to do active rendering
		// because we don't want to do it on the EDT
		Thread thread = new Thread() {
			public void run() {
				// perform an infinite loop stopped
				// render as fast as possible
				while (!isStopped()) {
					gameLoop();
					// you could add a Thread.yield(); or
					// Thread.sleep(long) here to give the
					// CPU some breathing room
					Thread.yield();
				}
			}
		};
		// set the game loop thread to a daemon thread so that
		// it cannot stop the JVM from exiting
		thread.setDaemon(true);
		// start the game loop
		thread.start();
	}

	/**
	 * The method calling the necessary methods to update
	 * the game, graphics, and poll for input.
	 */
	protected void gameLoop() {
		// get the graphics object to render to
		Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();

		// before we render everything im going to flip the y axis and move the
		// origin to the center (instead of it being in the top left corner)
		AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
		AffineTransform move = AffineTransform.getTranslateInstance(400, -300);
		g.transform(yFlip);
		g.transform(move);

		// now (0, 0) is in the center of the screen with the positive x axis
		// pointing right and the positive y axis pointing up

		// render anything about the Example (will render the World objects)
		this.render(g);

		// dispose of the graphics object
		g.dispose();

		// blit/flip the buffer
		BufferStrategy strategy = this.canvas.getBufferStrategy();
		if (!strategy.contentsLost()) {
			strategy.show();
		}

		// Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();

        // update the World

        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
    	// convert from nanoseconds to seconds
    	double elapsedTime = diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.world.update(elapsedTime);
	}



	public String removeLastChar(String str) {
		if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	/**
	 * Renders the example.
	 * @param g the graphics object to render to
	 */


	public void drawEnergyGrid(Graphics2D g)
	{
		for (int i=0; i<energyGrid.length;i++)
		{
			for (int j=0; j<energyGrid[0].length;j++)
			{
				if (energyGrid[i][j]==0)
				{
					g.setColor(Color.WHITE);
				}
				if (energyGrid[i][j]>0 && energyGrid[i][j]<=20)
				{
					g.setColor(Color.YELLOW);
				}
				if (energyGrid[i][j]>20 && energyGrid[i][j]<=40)
				{
					g.setColor(Color.RED);
				}
				int gridSize= (int) (20*globalScale);
				g.fillRect((-(energyGrid.length-1)/2+i)*gridSize-(int) (10*globalScale)+muoversiDestraESinistra, (-(energyGrid.length-1)/2+j)*gridSize-(int) (10*globalScale)+muoversiSuEGiu, gridSize, gridSize);
			}
		}
	}

	public int[] convertFromCoordinatesToEnergyGrid(int x,int y)
	{
		int[] a=new int[2];
		a[0]=x+(energyGrid.length-1)/2;
		a[1]=y+(energyGrid.length-1)/2;
		//System.out.println("x "+x);
		//System.out.println("y "+y);
		//System.out.println("x "+a[0]);
		//System.out.println("y "+a[1]);
		return a;
	}


	public void updateEnergyGrid(int coordX,int coordY)
	{
		int[] a=convertFromCoordinatesToEnergyGrid(coordX,coordY);
		int x=a[0];
		int y=a[1];
		if ((0>x)||(0>y)||((energyGrid.length-1)<x)||((energyGrid.length-1)<y))
		{

		}
		else
		{
			if (energyGrid[x][y]>0)
			{
				energyGrid[x][y]=energyGrid[x][y]-1;
			}
		}

	}


	protected void render(Graphics2D g) {
		// lets draw over everything with a white background
		if (!initalPaint)
		{
			g.setColor(Color.WHITE);
			g.fillRect(-800, -600, 1200, 900);

			initalPaint=true;
		}
		g.setColor(Color.WHITE);
		g.fillRect(-800, -600, 1200, 900);

		drawEnergyGrid(g);

		// lets move the view up some
		g.translate(muoversiDestraESinistra, muoversiSuEGiu);
		g.scale(globalScale,globalScale);
		// draw all the objects in the world
		for (int i = 0; i < this.world.getBodyCount(); i++) {
			// get the object
			GameObject go = (GameObject) this.world.getBody(i);
			// draw the object
			//System.out.println("aa: "+ go.getInitialTransform());



			String line = go.getInitialTransform().toString();
			String pattern = "([0-9\\.\\-])*?]";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);
			float x=0;
			float y=0;
			// Now create matcher object.
			Matcher m = r.matcher(line);
			if (m.find( )) {
				String xRaw=m.group(0);
				String xs=xRaw.substring(0,xRaw.length()-1);
				x=Float.parseFloat(xs);
				//System.out.println("Found value1: " + x);//POSITION OF OBJECT
				Pattern p = Pattern.compile(m.group(0));
				Matcher m2 = p.matcher(line);
				line = m2.replaceAll("");


				Matcher m3 = r.matcher(line);
				if (m3.find( )) {
					String yRaw=m3.group(0);
					String ys=yRaw.substring(0,yRaw.length()-1);
					y=Float.parseFloat(ys);
					//System.out.println("Found value2: " + y);//POSITION OF OBJECT
					int yBlock=Math.round(y/2);
					int xBlock=Math.round(x/2);

					updateEnergyGrid(xBlock,yBlock);
				}

			}else {
				System.out.println("NO MATCH");
			}

//			if (i==4)
//			{
//				go.applyForce(new Vector2(10, 2));
//			}
			if (i==4)
			{


				float gravita = (float) 10;
				if (mouseYes)
				{

//				int intx=(int) x;
//				int inty=((int) y);//60 e 540
					int yBlock=Math.round(y/2);
					int xBlock=Math.round(x/2);
					int[] intxa=convertFromCoordinatesToEnergyGrid(xBlock,yBlock);
					int newx=(xBlock+30)*8;
					int newy=-(yBlock-30)*8;
//				System.out.println(newx+","+newy);
//				System.out.println((int)x+","+(int)y);

					int diffx= mouseX-newx;
					int diffy= -mouseY+newy;
					System.out.println("x) m"+mouseX+" - b"+newx+" = "+diffx);
					System.out.println("y) -m"+mouseY+" + b"+newy+" = "+diffy+"\n");
					go.applyForce(new Vector2(diffx*gravita, diffy*gravita));
					//go.get

				}
			}

//			if (Settings.isDPressed())
//			{
//				go.applyForce(new Vector2(gravita, 0));
//			}
//			if (Settings.isSPressed())
//			{
//				go.applyForce(new Vector2(0, -gravita));
//			}
//			if (Settings.isAPressed())
//			{
//				go.applyForce(new Vector2(-gravita, 0));
//			}




			//System.out.println("aa: "+ go.getId());
			go.render(g);
		}
	}

	/**
	 * Stops the example.
	 */
	public synchronized void stop() {
		this.stopped = true;
	}

	/**
	 * Returns true if the example is stopped.
	 * @return boolean true if stopped
	 */
	public synchronized boolean isStopped() {
		return this.stopped;
	}

	/**
	 * Entry point for the example application.

	public static void main(String[] args) {

	}*/


	public void moveCameraRight()
	{
		System.out.println("moveCameraRight");
		muoversiDestraESinistra=muoversiDestraESinistra+10;
	}

	public void moveCameraLeft()
	{
		System.out.println("moveCameraLeft");
		muoversiDestraESinistra=muoversiDestraESinistra-10;
	}

	public void moveCameraUp()
	{
		System.out.println("moveCameraUp");
		muoversiSuEGiu=muoversiSuEGiu+10;
	}

	public void moveCameraDown()
	{
		System.out.println("moveCameraDown");
		muoversiSuEGiu=muoversiSuEGiu-10;
	}

}
