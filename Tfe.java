import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tfe implements KeyListener{

	static int[][] canvas;
	static JFrame frame;
	static boolean moved = false;
	static int score = 0;
	static int SIZE = 0;
	static boolean released = false;
	static boolean play = true;
	static String dir = "";
	
	public static class Paint extends JPanel{
		private static final long serialVersionUID = 1L;
		public void paintComponent(Graphics g){
			g.setColor(Color.black);
			g.fillRect(0,0,frame.getWidth(),frame.getHeight());
			for(int y = 0; y < SIZE; y++){
				for(int x = 0; x < SIZE; x++){
					if(canvas[y][x] != 0){
						int buffer = 20;
						g.setFont(new Font("Arial",Font.PLAIN,12));
						switch(canvas[y][x]){
							case 2: g.setColor(new Color(255,215,0));
									break;
							case 4: g.setColor(new Color(255,165,0));
									break;
							case 8: g.setColor(new Color(250,125,0));
									break;
							case 16: g.setColor(new Color(255,89,0));
									 buffer = 16;
									break;
							case 32: g.setColor(new Color(245,0,0));
									 buffer = 16;
									break;
							case 64: g.setColor(new Color(178,34,34));
									 buffer = 16;
									break;
							case 128: g.setColor(new Color(105,105,255));
									  buffer = 13;
									break;
							case 256: g.setColor(new Color(10,10,225));
									  buffer = 13;
									break;
							case 512: g.setColor(new Color(153,225,27));
									  buffer = 13;
									break;
							case 1024: g.setColor(new Color(124,252,100));
									  buffer = 9;
									break;
							case 2048: g.setColor(new Color(0,220,220));
									  buffer = 9;
									break;
							case 4096: g.setColor(new Color(220,50,220));
									  buffer = 9;
									break;
							case 8192: g.setColor(new Color(190,0,200));
							  		  buffer = 9;
							  		break;
							case 16384: g.setColor(new Color(176,196,222));
					  		  		  buffer = 6;
					  		  		break;
					  		case 32768: g.setColor(new Color(90,90,100));
			  		  		  		  buffer = 6;
			  		  		  		break;
							case 65536: g.setColor(new Color(170,170,180));
									  buffer = 5;
									break;
							case 131072: g.setColor(new Color(200,215,215));
							g.setFont(new Font("Arial",Font.BOLD,10));
							  		  buffer = 5;
							  		break;
							case 262144: g.setColor(new Color(255,255,255));
							g.setFont(new Font("Arial",Font.BOLD,10));
							  		  buffer = 5;
							  	    break;
							case 524288: g.setColor(new Color(218,165,32));
							g.setFont(new Font("Arial",Font.BOLD,10));
							  		  buffer = 5;
							  		break;
						}
						g.fillRect(x*50, y*50, 50, 50);
						g.setColor(Color.white);
						if(canvas[y][x] >= 131072) g.setColor(new Color(0,0,200));
						g.drawString(canvas[y][x]+"", x*50+buffer, y*50+27);	//buffer as in margin
					}
					g.setColor(Color.DARK_GRAY);
					g.drawRect(x*50,y*50,50,50);
					g.drawRect(x*50+1,y*50+1,48,48);
				}
			}
			g.setColor(Color.white);
			g.drawString("Score: "+score+"",14,215+50*(SIZE-4));
		}
	}
	
	public static void generate(){
		boolean allFilled = true;
		for(int y = 0; y < SIZE; y++){
			for(int x = 0; x < SIZE; x++){
				if(canvas[y][x] == 0){
					allFilled = false;
					y = 100;
					break;
				}
			}
		}
		if(allFilled) return;
		int y = (int)(Math.random()*SIZE);
		int x = (int)(Math.random()*SIZE);
		if(dir.equals("w")) y = SIZE-1;
		else if(dir.equals("s")) y = 0;
		else if(dir.equals("d")) x = 0;
		else x = SIZE-1;
		if(canvas[y][x] != 0){
			generate();
		}
		else{
			if((int)(Math.random()*5) == 0) canvas[y][x] = 4;
			else canvas[y][x] = 2;
		}
	}
	
	public static void down(){
		boolean changes = false;										//test if player made actual move
		for(int x = 0; x < SIZE; x++){
			ArrayList<Integer> arr = new ArrayList<Integer>();
			for(int y = SIZE-1; y >= 0; y--){
				if(canvas[y][x] != 0){
					arr.add(canvas[y][x]);
					if(y+1 < SIZE && canvas[y+1][x] == 0) changes = true;	//blank piece, also counts as valid move
				}
			}
			for(int i = 0; i < arr.size(); i++){
				if(i+1 < arr.size() && (int) arr.get(i) == (int) arr.get(i+1)){
					int value = arr.get(i)*2;
					arr.remove(i);
					arr.remove(i);
					arr.add(i,value);
					score += value;
					changes = true;
				}
			}
			for(int y = 0; y < SIZE; y++) canvas[y][x] = 0;
			for(int y = SIZE-1; y >= 0; y--){
				if(arr.size() > 0){
					canvas[y][x] = arr.get(0);
					arr.remove(0);
				}
				else{
					break;
				}
			}
		}
		if(changes == false) moved = false;								//we dont generate piece if player didnt move
	}
	
	public static void up(){
		boolean changes = false;									
		for(int x = 0; x < SIZE; x++){
			ArrayList<Integer> arr = new ArrayList<Integer>();
			for(int y = 0; y < SIZE; y++){
				if(canvas[y][x] != 0){
					arr.add(canvas[y][x]);
					if(y-1 >= 0 && canvas[y-1][x] == 0) changes = true;
				}
			}
			for(int i = 0; i < arr.size(); i++){
				if(i+1 < arr.size() && (int) arr.get(i) == (int) arr.get(i+1)){
					int value = arr.get(i)*2;
					arr.remove(i);
					arr.remove(i);
					arr.add(i,value);
					score += value;
					changes = true;
				}
			}
			for(int y = 0; y < SIZE; y++) canvas[y][x] = 0;
			for(int y = 0; y < SIZE; y++){
				if(arr.size() > 0){
					canvas[y][x] = arr.get(0);
					arr.remove(0);
				}
				else{
					break;
				}
			}
		}
		if(changes == false) moved = false;								
	}
	
	public static void right(){
		boolean changes = false;									
		for(int y = 0; y < SIZE; y++){
			ArrayList<Integer> arr = new ArrayList<Integer>();
			for(int x = SIZE-1; x >= 0; x--){
				if(canvas[y][x] != 0){
					arr.add(canvas[y][x]);
					if(x+1 < SIZE && canvas[y][x+1] == 0) changes = true;		
				}
			}
			for(int i = 0; i < arr.size(); i++){
				if(i+1 < arr.size() && (int) arr.get(i) == (int) arr.get(i+1)){
					int value = arr.get(i)*2;
					arr.remove(i);
					arr.remove(i);
					arr.add(i,value);
					score += value;
					changes = true;
				}
			}
			for(int x = 0; x < SIZE; x++) canvas[y][x] = 0;
			for(int x = SIZE-1; x >= 0; x--){
				if(arr.size() > 0){
					canvas[y][x] = arr.get(0);
					arr.remove(0);
				}
				else{
					break;
				}
			}
		}
		if(changes == false) moved = false;		
	}
	
	public static void left(){
		boolean changes = false;									
		for(int y = 0; y < SIZE; y++){
			ArrayList<Integer> arr = new ArrayList<Integer>();
			for(int x = 0; x < SIZE; x++){
				if(canvas[y][x] != 0){
					arr.add(canvas[y][x]);
					if(x-1 >= 0 && canvas[y][x-1] == 0) changes = true;		
				}
			}
			for(int i = 0; i < arr.size(); i++){
				if(i+1 < arr.size() && (int) arr.get(i) == (int) arr.get(i+1)){
					int value = arr.get(i)*2;
					arr.remove(i);
					arr.remove(i);
					arr.add(i,value);
					score += value;
					changes = true;
				}
			}
			for(int x = 0; x < SIZE; x++) canvas[y][x] = 0;
			for(int x = 0; x < SIZE; x++){
				if(arr.size() > 0){
					canvas[y][x] = arr.get(0);
					arr.remove(0);
				}
				else{
					break;
				}
			}
		}
		if(changes == false) moved = false;		
	}
	
	public static void init(){
		frame = new JFrame();
		frame.setSize(210+50*(SIZE-4),255+50*(SIZE-4));
		frame.setTitle("2048");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new Tfe());
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		//showColors();
		boolean enterMainLoop = true;
		try{
			if(args[0].equals("a string")){}
		}
		catch(Exception e){
			if(true){
				SIZE = 4;
				canvas = new int[SIZE][SIZE];
				init();
				generate();
				frame.add(new Paint());
				frame.setVisible(true);
				while(true){
					if(moved){
						generate();
						moved = false;
						frame.add(new Paint());
						frame.setVisible(true);
					}
					try{
						Thread.sleep(50);
					}
					catch(Exception e2){	
						//do nothing
					}
				}
			}
		}
		if(!enterMainLoop) return;
		if(args.length != 2) System.out.println("please call size of grid then manual or auto");
		else if(args[1].equals("auto") || args[1].equals("manual")){
			SIZE = Integer.parseInt(args[0]);
			canvas = new int[SIZE][SIZE];
			init();
			generate();
			frame.add(new Paint());
			frame.setVisible(true);
			if(args[1].equals("auto")) auto();
			while(true){
				if(moved){
					generate();
					moved = false;
					frame.add(new Paint());
					frame.setVisible(true);
				}
				try{
					Thread.sleep(50);
				}
				catch(Exception e){	
				}
			}
		}
		else{
			System.out.println("please call size of grid then manual or auto");
		}
	}

	public static void showColors(){
		canvas[0][0] = 2;
		canvas[0][1] = 4;
		canvas[0][2] = 8;
		canvas[0][3] = 16;
		canvas[1][0] = 32;
		canvas[1][1] = 64;
		canvas[1][2] = 128;
		canvas[1][3] = 0;
		canvas[2][0] = 256;
		canvas[2][1] = 512;
		canvas[2][2] = 1024;
		canvas[2][3] = 2048;
		canvas[3][0] = 4096;
		canvas[3][1] = 8192;
		canvas[3][2] = 16384;
		canvas[3][3] = 2;
	}

	public static void auto(){
		while(play){
			try{
				generate();
				while(true){
					int sto = score;
					up();
					right();
					if(sto == score) break;
					generate();
				}
				while(true){
					int sto = score;
					up();
					left();
					if(sto == score) break;
					generate();
				}
				while(true){
					int sto = score;
					down();
					right();
					if(sto == score) break;
					generate();
				}
				while(true){
					int sto = score;
					down();
					left();
					if(sto == score) break;
					generate();
				}
				frame.add(new Paint());
				frame.setVisible(true);
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		play = false;
		if(released){
			released = false;
			if(e.getKeyCode() == KeyEvent.VK_S){
				moved = true;
				down();
				dir = "s";
				try{
					Thread.sleep(10);
				}catch(Exception exception){
					
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_W){
				moved = true;
				up();
				dir = "w";
				try{
					Thread.sleep(10);
				}catch(Exception exception){
					
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_D){
				moved = true;
				right();
				dir = "d";
				try{
					Thread.sleep(10);
				}catch(Exception exception){
					
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_A){
				moved = true;
				left();
				dir = "a";
				try{
					Thread.sleep(10);
				}catch(Exception exception){
					
				}
			}
		}
	}
	public void keyReleased(KeyEvent arg0) {
		released = true;
	}
	public void keyTyped(KeyEvent arg0) {
	}
}
