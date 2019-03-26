package multiClient;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import multiServer.Stone;

/**
 * This class holds the game logic needed for gameplay between the user and the
 * in house AI. Some methods remain similar to GamePente Local, however
 * accommodations for AI are also incorporated. The logic of the AI's decisions
 * are handled in multiple classes, which fluently interact with the necessary
 * steps to ensure the legitimacy and ability of the human player to make moves.
 * An instance of this class is created in NewPenteWindow.
 */
public class GamePenteLocalAI {

	// this are the two rule set, representing the biggest two winning set.
	private final int ONE = 1;
	private final int TWO = 2;

	// in this AI, we are always looking for the max score for computer player to
	// place the stone
	// max means the computer has a better opportunity to win the game
	private static int MaxComputerScore = 0;

	// there is another 2d array stores the computer score around the point we
	// placed,
	private int[][] computerScore = new int[13][13];

	// we need math random to pick one of the highest score around the point we
	// placed, so it is totally random
	Random rand = new Random();

	int comx, comy;

	/**
	 * This variable is a JFrame that holds the GUI.
	 */
	private PenteWindow gui;

	/**
	 * ClickListener handles click events in PenteWindow.
	 */
	private static ClickListener pointer;

	/**
	 * This boolean value indicates whose turn it is. As a result, there is no
	 * need for any Player objects. 1 - black's turn. 0 - white's turn.
	 */
	private boolean turnStatus;
	private static boolean winDetected = false;
	/**
	 * This integer holds the number of white captures.
	 */
	private int whiteCaps;
	/**
	 * This integer holds the number of black captures.
	 */
	private int blackCaps;
	PenteWindow window;

	private int[][] board = new int[13][13];
	int[] computermove;
	private ArrayList<ChessXYdataexchange> chessList =
	    new ArrayList<ChessXYdataexchange>();

	/**
	 * This is the main method for the class
	 */
	public static void main ( String[] args ) {
		GamePenteLocalAI game = new GamePenteLocalAI();
	}

	/*
	 * creation of separate gameboard for use of AI only
	 */
	private void initdataBoard () {
		for ( int i = 0 ; i < 13 ; i++ ) {
			for ( int j = 0 ; j < 13 ; j++ ) {
				board[i][j] = 0;
			}
		}
	}

	/*
	 * displays the AI board to the console for debugging purposes
	 */
	private void printdataBoard () {
		for ( int i = 0 ; i < 13 ; i++ ) {
			for ( int j = 0 ; j < 13 ; j++ ) {
				System.out.print(board[i][j]);
			}
			System.out.print("\n");
		}
	}

	/*
	 * this is another secondary board that holds determined success of certain
	 * moves after the players moves have been made
	 */
	private void printComputerBoard () {
		for ( int i = 0 ; i < 13 ; i++ ) {
			for ( int j = 0 ; j < 13 ; j++ ) {
				System.out.print(computerScore[i][j]);
			}
			System.out.print("\n");
		}
	}

	/**
	 * Default constructor for GamePenteLocalAI. Calls the init() method, which
	 * should do two things: -build and display the start menu. -prep the Board
	 * JPanel.
	 */
	public GamePenteLocalAI () {
		init();
		initdataBoard();
	}

	/**
	 * This method serves to initialize the GUI portion of this component.
	 */
	public void init () {
		pointer = new ClickListener();
		gui = new PenteWindow();
		setupStartupListeners();
	}

	/**
	 * RESET When called, this method effectively resets the state of the game. To
	 * do this: -cap counts must be reset -go through the entire board, setting
	 * each status to 0. -call Board.update()
	 */
	public void reset () {
		winDetected = false;
		whiteCaps = 0;
		blackCaps = 0;
		turnStatus = false;
		int bWidth = gui.getPane().getGridWidth();
		gui.setMessage("New Game!",whiteCaps,blackCaps);
		for ( int i = 0 ; i < bWidth ; i++ ) {
			for ( int j = 0 ; j < bWidth ; j++ ) {
				board[i][j] = 0;
				computerScore[i][j] = 0;
				if ( i == (bWidth - 1) / 2 && j == (bWidth - 1) / 2 ) {
					gui.getPane().getStoneAt(i,j).setStatus(2);

				} else {
					gui.getPane().getStoneAt(i,j).setStatus(0);
				}
			}
		}
		gui.getPane().revalidate();
		gui.getPane().repaint();
	}

	/*
	 * this method sets the AI's first move which is always the center of the
	 * board
	 */
	public void resetblack () {
		gui.getPane().getStoneAt(10,10).setStatus(2);
	}

	/**
	 * @return whether or not it is black's turn.
	 */
	public boolean isBlacksTurn () {
		return turnStatus;
	}

	/**
	 * @param boardPanel
	 * @return
	 */
	private PlayPane setBoard ( PlayPane boardPanel ) {
		// TODO Auto-generated method stub
		gui.setBoard(boardPanel);
		return gui.getPane();
	}

	/**
	 * This method is used to set the listener for each of the JButtons in the
	 * start menu to be pointer.
	 */
	public void setupStartupListeners () {
		ArrayList<JButton> btns = gui.getStartMenuButtons();
		for ( int i = 0 ; i < btns.size() ; i++ ) {
			btns.get(i).addMouseListener(pointer);
		}
	}

	/**
	 * This method is used to set the listener for each of the JPanels on the
	 * PlayPane.
	 */
	public void addClickListener () {
		Stone[] tiles = gui.getTileBoard();
		for ( int x = 0 ; x < 169 ; x++ ) {
			Stone tile = tiles[x];
			tile.addMouseListener(pointer);
			// System.out.println(pointer);
		}
	}

	/**
	 * This method is used to set the listener for each JMenuItem.
	 */
	public void setupMenuListeners () {
		ArrayList<JMenuItem> items = gui.getMenuItems();

		for ( int i = 0 ; i < items.size() ; i++ ) {
			items.get(i).addActionListener(pointer);
		}
	}

	/*
	 * this method creates a new action listener for a specified button
	 * @param button to be given the listener
	 */
	public void addNewGameListener ( JButton button ) {
		button.addActionListener(pointer);
	}

	/**
	 * @author am8525 The ClickListener class is the MouseListener for the Board
	 *         class. It listens for mouse clicks. Upon hearing one, it retrieves
	 *         the location within the GridLayout of the Stone that was clicked,
	 *         and passes that location to PenteUtil.
	 */
	public class ClickListener implements MouseListener, ActionListener {
		/**
		 * This will be the primary method that we implement for our MouseListener
		 * class. Within the body, we with make a method
		 */
		public ClickListener () {
			super();
		}

		/**
		 * This mouseClicked method contains most of the sequential checking. Makes
		 * several calls to PenteUtil to perform various checks. Also used for other
		 * types of ActionEvents with other types of components, such as JMenuItems
		 * and JButtons
		 */
		int x;
		int y;
		String[] xAndY;

		public void mouseClicked ( MouseEvent arg0 ) {

			if ( arg0.getSource() instanceof JButton ) {

				JButton btn = (JButton) arg0.getSource();
				if ( btn.getText() == "Quit" ) {
					QuitDialog quitPan = new QuitDialog();
					quitPan.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					quitPan.setVisible(true);
					quitPan.validate();
					return;
				}

				if ( btn.getText() == "How to Play" ) {
					gui.triggerTutorial();
					return;
				}

				if ( btn.getText() == "Play Game" ) {

					gui.setupGame(true);
					addClickListener();
					setupMenuListeners();
					return;
				}
			}

			if ( winDetected ) {
				return;
			}

			JPanel clickedPanel = (JPanel) arg0.getSource();

			/**
			 * if things happen the way they should and the clicked JPanel is a Stone
			 * on the Board, then getName() should return something of the form: 1,2
			 * OR 4,2 where the first number is the x, and the 2nd is the y.
			 */
			String coordinateString = clickedPanel.getName();
			xAndY = coordinateString.split(",");
			x = Integer.parseInt(xAndY[0]);
			y = Integer.parseInt(xAndY[1]);

			int move;
			if ( turnStatus ) {
				move = 2;
			} else {
				move = 1;
			}

			/**
			 * VALIDATE MOVE CHECK.
			 */
			if ( !PenteUtil.validateMove(x,y,move,gui.getPane()) ) {
				gui.setInfoMessage("Illegal move!");
				return;
			}

			Object[] keys =
			    PenteUtil.getNonEmptyNeighbors(x,y,gui.getPane()).keySet().toArray();

			/**
			 * CHECK CAPTURES, THEN PERFORM NECESSARY UPDATES TO CAPCOUNT AND BOARD.
			 * and AI changes the originally data bases.
			 */
			ArrayList<Point> caps =
			    PenteUtil.checkCaptures(x,y,move,gui.getPane(),keys);
			if ( !caps.isEmpty() ) {
				Point p1 = caps.get(0);
				Point p2 = caps.get(1);
				int x = (int) p1.getX();
				int y = (int) p1.getY();
				board[x][y] = 0;
				int a = (int) p2.getX();
				int b = (int) p2.getY();
				board[a][b] = 0;
			}

			blackCaps = blackCaps + (caps.size() / 2);
			whiteCaps = whiteCaps + (caps.size() / 2);

			/**
			 * The center sitting of the board is black which is 2
			 */
			board[6][6] = 2;

			/**
			 * the white start placing a stone and draw it on the board
			 */
			board[x][y] = 1;
			gui.getPane().getStoneAt(x,y).setStatus(1);

			/**
			 * computer capture is zero
			 */
			MaxComputerScore = 0;
			for ( int i = 0 ; i < 13 ; ++i ) {
				for ( int j = 0 ; j < 13 ; ++j ) {
					computerScore[i][j] = 0;
				}
			}

			getScore();

			// prints two AI boards to simulate the game
			System.out.println("***************");
			printComputerBoard();
			System.out.println("***************");
			System.out.println("The data board:");
			printdataBoard();
			for ( int i = 0 ; i < 13 ; ++i ) {
				for ( int j = 0 ; j < 13 ; ++j ) {

					if ( computerScore[i][j] == MaxComputerScore ) {
						for ( int a = 0 ; a < 13 ; ++a ) {
							for ( int b = 0 ; b < 13 ; ++b ) {
								if ( board[a][b] == 0 ) {
									ChessXYdataexchange onepeice = new ChessXYdataexchange(i,j);
									chessList.add(onepeice);
								}
							}
						}
					}
				}
			}

			int n = rand.nextInt(chessList.size());
			comx = chessList.get(n).x;
			comy = chessList.get(n).y;
			gui.getPane().getStoneAt(comx,comy).setStatus(2);
			board[comx][comy] = 2;
			chessList.clear();

			/**
			 * This Check win method works on the AI
			 */

			if ( AIisWin(1,x,y) ) {
				System.out.print("white win");
				gui.setInfoMessage(" YOU WIN! YOU BEAT THE AI!!!");
				winDetected = true;
				WinDialog winDialog = new WinDialog(1);
				winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				winDialog.setVisible(true);
				winDialog.validate();
				addNewGameListener(winDialog.getNewGameButton());
				return;
			}

			if ( AIisWin(2,comx,comy) ) {
				System.out.print("Computer AI win!");
				gui.setInfoMessage("YOU LET AI WINS!!!!");
				winDetected = true;
				WinDialog winDialog = new WinDialog(2);
				winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				winDialog.setVisible(true);
				winDialog.validate();
				addNewGameListener(winDialog.getNewGameButton());
				return;
			}

			/**
			 * FINALLY, CHECK COMBOS. (TRIA AND TESSERA)
			 */
			int comboCode = PenteUtil.checkCombos(x,y,gui.getPane(),keys);
			switch ( comboCode ) {
			case 1:

				gui.setMessage("YOU " + " has a tria!",whiteCaps,blackCaps);
				break;

			case 2:

				gui.setMessage("YOU " + " has a tessera!",whiteCaps,blackCaps);
				break;

			default:

				gui.setMessage("YOU " + " have gone.....",whiteCaps,blackCaps);
				break;
			}
		}

		/**
		 * This is the calculation method, see if the game is over for the data set
		 * in AI.
		 *
		 * @param f
		 * @param x
		 * @param y
		 * @return boolean
		 */
		public boolean AIisWin ( int f, int x, int y ) {
			int i, count = 1;
			boolean up, down, right, left, rup, lup, rdown, ldown;
			up = down = right = left = rup = lup = rdown = ldown = true;
			for ( i = 1 ; i < 5 ; ++i ) {
				if ( (y + i) < 13 ) {
					if ( board[x][y + i] == f && down ) count++;
					else down = false;
				}
				if ( (y - i) >= 0 ) {
					if ( board[x][y - i] == f && up ) count++;
					else up = false;
				}
			}
			if ( count >= 5 ) {
				return true;
			}
			count = 1;

			for ( i = 1 ; i < 5 ; ++i ) {
				if ( (x + i) < 13 ) {
					if ( board[x + i][y] == f && right ) count++;
					else right = false;
				}
				if ( (x - i) >= 0 ) {
					if ( board[x - i][y] == f && left ) count++;
					else left = false;
				}
			}
			if ( count >= 5 ) {
				return true;
			}
			count = 1;

			for ( i = 1 ; i < 5 ; ++i ) {
				if ( (x + i) < 13 && (y + i) < 13 ) {
					if ( board[x + i][y + i] == f && rdown ) count++;
					else rdown = false;
				}
				if ( (x - i) >= 0 && (y - i) >= 0 ) {
					if ( board[x - i][y - i] == f && lup ) count++;
					else lup = false;
				}
			}
			if ( count >= 5 ) {
				return true;
			}
			count = 1;

			for ( i = 1 ; i < 5 ; ++i ) {
				if ( (x + i) < 13 && (y - i) >= 0 ) {
					if ( board[x + i][y - i] == f && rup ) count++;
					else rup = false;
				}
				if ( (x - i) >= 0 && (y + i) < 13 ) {
					if ( board[x - i][y + i] == f && ldown ) count++;
					else ldown = false;
				}
			}
			if ( count >= 5 ) {
				return true;
			}

			return false;
		}

		/**
		 * @param bw
		 *          black and white
		 * @param x
		 *          x cord
		 * @param y
		 *          y cord
		 * @param num
		 * @param hORc
		 * @return
		 */
		private boolean isONEOrTWO ( int bw, int x, int y, int num, int hORc ) {

			num += 1;
			int i, count = 1;
			boolean terminal1 = false;
			boolean terminal2 = false;
			boolean up, down, right, left, rup, lup, rdown, ldown;
			up = down = right = left = rup = lup = rdown = ldown = true;
			/* up down */
			for ( i = 1 ; i < num ; ++i ) {
				if ( (y + i) < 13 ) {
					if ( board[x][y + i] == bw && down ) count++;
					else {
						if ( board[x][y + i] == 0 && down ) {
							terminal1 = true;
						}
						down = false;
					}
				}
				if ( (y - i) >= 0 ) {
					if ( board[x][y - i] == bw && up ) count++;
					else {
						if ( board[x][y - i] == 0 && up ) {
							terminal2 = true;
						}
						up = false;
					}
				}
			}
			if ( count == num - 1 && hORc == ONE && terminal1 && terminal2 ) {
				return true;
			}
			if ( count == num - 1 && hORc == TWO
			    && ((terminal1 && !terminal2) || (!terminal1 && terminal2)) ) {
				return true;
			}
			count = 1;
			terminal1 = false;
			terminal2 = false;
			/* left and right */
			for ( i = 1 ; i < num ; ++i ) {
				if ( (x + i) < 13 ) {
					if ( board[x + i][y] == bw && right ) count++;
					else {
						if ( board[x + i][y] == 0 && right ) {
							terminal1 = true;
						}
						right = false;
					}
				}
				if ( (x - i) >= 0 ) {
					if ( board[x - i][y] == bw && left ) count++;
					else {
						if ( board[x - i][y] == 0 && left ) {
							terminal2 = true;
						}
						left = false;
					}
				}
			}
			if ( count == num - 1 && hORc == ONE && terminal1 && terminal2 ) {
				return true;
			}
			if ( count == num - 1 && hORc == TWO
			    && ((terminal1 && !terminal2) || (!terminal1 && terminal2)) ) {
				return true;
			}
			count = 1;
			terminal1 = false;
			terminal2 = false;
			/* leftup and rightdown */
			for ( i = 1 ; i < num ; ++i ) {
				if ( (x + i) < 13 && (y + i) < 13 ) {
					if ( board[x + i][y + i] == bw && rdown ) count++;
					else {
						if ( board[x + i][y + i] == 0 && rdown ) {
							terminal1 = true;
						}
						rdown = false;
					}
				}
				if ( (x - i) >= 0 && (y - i) >= 0 ) {
					if ( board[x - i][y - i] == bw && lup ) count++;
					else {
						if ( board[x - i][y - i] == 0 && lup ) {
							terminal2 = true;
						}
						lup = false;
					}
				}
			}
			if ( count == num - 1 && hORc == ONE && terminal1 && terminal2 ) {
				return true;
			}
			if ( count == num - 1 && hORc == TWO
			    && ((terminal1 && !terminal2) || (!terminal1 && terminal2)) ) {
				return true;
			}
			count = 1;
			terminal1 = false;
			terminal2 = false;
			/* rightup and leftdown */
			for ( i = 1 ; i < num ; ++i ) {
				if ( (x + i) < 13 && (y - i) >= 0 ) {
					if ( board[x + i][y - i] == bw && rup ) count++;
					else {
						if ( board[x + i][y - i] == 0 && rup ) {
							terminal1 = true;
						}
						rup = false;
					}
				}
				if ( (x - i) >= 0 && (y + i) < 13 ) {
					if ( board[x - i][y + i] == bw && ldown ) count++;
					else {
						if ( board[x - i][y + i] == 0 && ldown ) {
							terminal2 = true;
						}
						ldown = false;
					}
				}
			}

			if ( count == num - 1 && hORc == ONE && terminal1 && terminal2 ) {
				return true;
			}
			if ( count == num - 1 && hORc == TWO
			    && ((terminal1 && !terminal2) || (!terminal1 && terminal2)) ) {
				return true;
			}

			return false;
		}

		/**
		 * simple getter for recieving the score that the AI has developed through
		 * its calculating algorithm
		 */
		public void getScore () {
			for ( int i = 0 ; i < 13 ; ++i ) {
				for ( int j = 0 ; j < 13 ; ++j ) {
					if ( board[i][j] == 0 ) {
						if ( AIisWin(2,i,j) ) {
							MaxComputerScore = 13;
							computerScore[i][j] = 13;
							return;
						} else if ( AIisWin(1,i,j) ) {
							MaxComputerScore = 12;
							computerScore[i][j] = 12;
						} else if ( isONEOrTWO(2,i,j,4,ONE) ) {
							MaxComputerScore =
							    (MaxComputerScore > 11 ? MaxComputerScore : 11);
							computerScore[i][j] = 11;
						} else if ( isONEOrTWO(2,i,j,4,TWO) ) {
							MaxComputerScore =
							    (MaxComputerScore > 10 ? MaxComputerScore : 10);
							computerScore[i][j] = 10;
						} else if ( isONEOrTWO(1,i,j,4,ONE) ) {
							MaxComputerScore = (MaxComputerScore > 9 ? MaxComputerScore : 9);
							computerScore[i][j] = 9;
						} else if ( isONEOrTWO(2,i,j,3,ONE) ) {
							MaxComputerScore = (MaxComputerScore > 8 ? MaxComputerScore : 8);
							computerScore[i][j] = 8;
						} else if ( isONEOrTWO(1,i,j,4,TWO) ) {
							MaxComputerScore = (MaxComputerScore > 7 ? MaxComputerScore : 7);
							computerScore[i][j] = 7;
						} else if ( isONEOrTWO(2,i,j,3,TWO) ) {
							MaxComputerScore = (MaxComputerScore > 6 ? MaxComputerScore : 6);
							computerScore[i][j] = 6;
						} else if ( isONEOrTWO(2,i,j,2,ONE) ) {
							MaxComputerScore = (MaxComputerScore > 5 ? MaxComputerScore : 5);
							computerScore[i][j] = 5;
						} else if ( isONEOrTWO(1,i,j,3,TWO) ) {
							MaxComputerScore = (MaxComputerScore > 4 ? MaxComputerScore : 4);
							computerScore[i][j] = 4;
						} else if ( isONEOrTWO(1,i,j,2,ONE) ) {
							MaxComputerScore = (MaxComputerScore > 3 ? MaxComputerScore : 3);
							computerScore[i][j] = 3;
						} else if ( isONEOrTWO(2,i,j,2,TWO) ) {
							MaxComputerScore = (MaxComputerScore > 2 ? MaxComputerScore : 2);
							computerScore[i][j] = 2;
						} else if ( isONEOrTWO(1,i,j,2,TWO) ) {
							MaxComputerScore = (MaxComputerScore > 1 ? MaxComputerScore : 1);
							computerScore[i][j] = 1;
						} else {
							computerScore[i][j] = 0;
						}
					}
				}
			}
		}

		/**
		 * get the AI placement
		 * 
		 * @return
		 */
		public int getx () {
			return comx;
		}

		public int gety () {
			return comy;
		}

		public void mouseEntered ( MouseEvent arg0 ) {

		}

		public void mouseExited ( MouseEvent arg0 ) {

		}

		public void mousePressed ( MouseEvent arg0 ) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased ( MouseEvent arg0 ) {
			// TODO Auto-generated method stub

		}

		/**
		 * this method deals solely with the JMenuItems while playing, as well as
		 * the "New Game" button on NewGameDialog and the "New Game" button on the
		 * WinDialog.
		 */
		public void actionPerformed ( ActionEvent arg0 ) {

			if ( arg0.getSource() instanceof JButton ) {
				if ( arg0.getActionCommand().equals("New Game Button1") ) {
					JButton btn = (JButton) arg0.getSource();
					NewGameDialog dia = (NewGameDialog) btn.getTopLevelAncestor();
					dia.dispose();
					reset();
				}
				if ( arg0.getActionCommand().equals("New Game Button2") ) {
					JButton btn = (JButton) arg0.getSource();
					WinDialog dia = (WinDialog) btn.getTopLevelAncestor();
					dia.dispose();
					reset();
				}
			}

			if ( arg0.getSource() instanceof JMenuItem ) {
				JMenuItem item = (JMenuItem) arg0.getSource();

				if ( item.getText() == "Quit" ) {
					QuitDialog qdia = new QuitDialog();
					return;
				}
				if ( item.getText() == "New Game" ) {
					NewGameDialog ngdia = new NewGameDialog();
					// JPanel holder = (JPanel) ngdia.getContentPane().getComponent(0);
					ngdia.getRootPane().getDefaultButton().addActionListener(pointer);
					ngdia.setVisible(true);
					ngdia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					ngdia.validate();
					return;

				}
				if ( item.getText() == "Game Rules" ) {
					Tutorial tut = new Tutorial();
					tut.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					tut.setVisible(true);
					tut.validate();
					return;
				}
			}

		}
	}
}
