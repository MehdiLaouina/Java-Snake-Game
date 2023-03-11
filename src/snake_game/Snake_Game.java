package snake_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Snake_Game extends JFrame{
	
	int vitesse=100,score=0,stage=1;
	int margeX=100,margeY=100;
	int nbCaseX=40,nbCaseY=25,largeCellule=20;
	int direction=1; //1=right, 2=down, 3=left, 4=up
	
	Color couleurtete=Color.red,couleurCorps=Color.green;
	Color couleurBG=Color.darkGray;
	Color couleurRepas=Color.yellow,couleurObstacle=Color.BLACK;
	
	
	ArrayList<Cellule> corps ;
	Cellule repas;
	ArrayList<Cellule> obstacle ;
	
	
	JPanel panneau;
	Timer horloge;
	
	public Snake_Game()
	{
		//parametres de bases d une fenetre
		this.setTitle("Snake Game ... Mehdi 22/3/2022"); // titre
		
		this.setSize(margeX*2 + largeCellule*nbCaseX +14, margeY*2 + largeCellule*nbCaseY+37); //14 = 7cellules + 7cellules de chaque cote (bordure de la fenetre), 37=30+7 ( bordure de la bare des taches)
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // close on exit
		
		this.setLocationRelativeTo(null); // center the window
		
		initialiser(); // intialisation du corps de serpent
		
		
		
		//intialisation de panneau
		panneau = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				drawGrid(g);
				drawObstacle(g);
				drawSnake(g);
				generateRepas();
				drawCellule(repas, g);
				
				
				
				
				
			}
		};
		this.setContentPane(panneau);//activer le panneau pour s'afficher dans la fenetre
		
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				
				if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction !=3) direction=1;
				if(e.getKeyCode() == KeyEvent.VK_DOWN && direction!=4) direction=2;
				if(e.getKeyCode() == KeyEvent.VK_LEFT && direction!=1) direction=3;
				if(e.getKeyCode() == KeyEvent.VK_UP && direction!=2) direction=4;
				
				
				
			}
		});
		
		
		horloge = new Timer(vitesse,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//avant de deplacer la tete, il faut deplacer le corps
				for(int i=corps.size()-1 ; i>0 /*i>=1*/ ; i--)
				{
					corps.get(i).x = corps.get(i-1).x;
					corps.get(i).y = corps.get(i-1).y;
				}
				
				//deplacement de tete
				if(direction ==1) corps.get(0).x++;
				if(direction ==2) corps.get(0).y++;
				if(direction ==3) corps.get(0).x--;
				if(direction ==4) corps.get(0).y--;
				//cas ou deplacement == bordure
				if(corps.get(0).x == nbCaseX) corps.get(0).x=0;
				if(corps.get(0).x == -1) corps.get(0).x=nbCaseX-1;
				if(corps.get(0).y == nbCaseY) corps.get(0).y=0;
				if(corps.get(0).y == -1) corps.get(0).y=nbCaseY-1;
				
				// food generation when it s eaten
				if(corps.get(0).x == repas.x && corps.get(0).y == repas.y)
				{
					generateRepas();
					corps.add(new Cellule(corps.get(corps.size()-1).x,corps.get(corps.size()-1).y,couleurCorps));
				}
				
				//Arret de game si snake touches obstacle
				for(Cellule c:obstacle)
				{
					if(corps.get(0).x-1 == c.x && corps.get(0).y-1 == c.y)
						horloge.stop();
						
				}
				
				
				
				repaint(); //rafraishir l'affichage pour afficher le deplacement du snake
			}
		});
		horloge.start(); //activate timer 
		
		
		this.setVisible(true);
	}
	//fin de constructeur
	
	public void initialiser()
	{
		corps = new ArrayList<Cellule>();
		corps.add(new Cellule(15,10,couleurtete));
		corps.add(new Cellule(14,10,couleurCorps));
		corps.add(new Cellule(13,10,couleurCorps));
		corps.add(new Cellule(12,10,couleurCorps));
		corps.add(new Cellule(11,10,couleurCorps));
		corps.add(new Cellule(10,10,couleurCorps));
		
		repas = new Cellule(0,0,couleurRepas);
		
		obstacle = new ArrayList<Cellule>();
		for(int i=nbCaseX/4 ; i<nbCaseX*3/4;i++)
		{
			obstacle.add(new Cellule(i,nbCaseY/3,couleurObstacle));
			obstacle.add(new Cellule(i,nbCaseY*2/3,couleurObstacle));
		}
	}
	
	public void drawObstacle(Graphics g)
	{
		for(Cellule c:obstacle)
		{
			drawCellule(c,g);
		}
	}
	
	
	public void generateRepas()
	{
		
		Random rand = new Random();
		boolean cond =false;
		while(!cond)
		{
			for(Cellule c:corps)
			{
				if(c.x == repas.x && c.y == repas.y)
				{
					int x1 = rand.nextInt(nbCaseX);
					int y1 = rand.nextInt(nbCaseY);
					repas.x=x1; repas.y=y1;
					generateRepas();
					break;
				}
				cond = true;
			}
			for(Cellule c:obstacle)
			{
				if(c.x == repas.x && c.y == repas.y)
				{
					int x1 = rand.nextInt(nbCaseX);
					int y1 = rand.nextInt(nbCaseY);
					repas.x=x1; repas.y=y1;
					generateRepas();
					break;
				}
			}
		}	
	}
	
	public void drawSnake(Graphics g)
	{
		for(Cellule c:corps)
			drawCellule(c,g);
	}
	
	public void drawGrid(Graphics g)
	{
		//Graphics g1 = panneau.getGraphics(); // soit ca soit on le passe en paramatre, c'est ce qu'on a fait
		g.setColor(couleurBG);
		g.fillRect(margeX, margeY, nbCaseX*largeCellule, nbCaseY*largeCellule);
		g.setColor(Color.gray);
		for(int i=0;i<nbCaseX;i++)
		{
			g.drawLine(margeX + i*largeCellule, margeY, margeX + i*largeCellule, margeY + nbCaseY*largeCellule);
			
		}
		for(int i=0;i<nbCaseY;i++)
		{
			g.drawLine(margeX, margeY + i*largeCellule, margeX + nbCaseX*largeCellule, margeY + i*largeCellule);
		}
	}
	
	
	
	
	public void drawCellule(Cellule C,Graphics g)
	{
		g.setColor(C.couleur);
		g.fillRect(margeX + largeCellule*C.x, margeY + largeCellule*C.y, largeCellule, largeCellule);
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		new Snake_Game();
		
		
	}
	class Cellule
	{
		int x,y;
		Color couleur;
		
		public Cellule(int x, int y, Color couleur) {
			super();
			this.x = x;
			this.y = y;
			this.couleur = couleur;
		}
		
	}
}

//creer initialiser repas a chaque fois snake mange repas