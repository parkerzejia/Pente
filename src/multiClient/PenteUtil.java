package multiClient;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import multiServer.Stone;

/**
 * This class, whether it is used as an object or interface, will serve to
 * perform specific systematic checks for any given move.
 */
public interface PenteUtil {

	/**
	 * Given the combination of the Board, attempted move, and Point(x,y), this
	 * method serves to analyze if any captures would occur from this move, and if
	 * so, how many would occur and where those captures would be located. NOTE:
	 * in this method, we assume the move to be valid.
	 * 
	 * @param x
	 *          coord of point being checked
	 * @param y
	 *          coord of point being checked
	 * @param move
	 *          : 1 - white, 2 - black
	 * @param Board
	 * @return ArrayList<Point> - these are the Points on the Board, where the
	 *         status should be set to zero from detected captures.
	 */
	public static ArrayList<Point> checkCaptures ( int x, int y, int move,
	                                               PlayPane board,
	                                               Object[] neighbors ) {

		ArrayList<Point> caps = new ArrayList<Point>();
		Object[] keys = neighbors;

		if ( keys.length == 0 ) {
			return caps;
		}

		Point point = null;
		int xOff = 0;
		int yOff = 0;
		int xTest = 0;
		int yTest = 0;

		for ( int i = 0 ; i < keys.length ; i++ ) {
			point = (Point) keys[i];

			xOff = (int) (point.getX() - x);
			yOff = (int) (point.getY() - y);
			xTest = (int) point.getX();
			yTest = (int) point.getY();

			if ( board.getStatusAt(xTest,yTest) == move ) {
				continue;
			}
			xTest = xTest + xOff;
			yTest = yTest + yOff;

			if ( board.getStatusAt(xTest,yTest) == move
			    || board.getStatusAt(xTest,yTest) == 0
			    || board.getStatusAt(xTest,yTest) == -1 ) {
				continue;
			}
			xTest = xTest + xOff;
			yTest = yTest + yOff;

			/**
			 * If this test passes, we have detected a cap.
			 */
			if ( move == board.getStatusAt(xTest,yTest) ) {
				xTest = xTest - xOff;
				yTest = yTest - yOff;
				board.getStoneAt(xTest,yTest).setStatus(0);
				caps.add(new Point(xTest,yTest));
				xTest = xTest - xOff;
				yTest = yTest - yOff;
				board.getStoneAt(xTest,yTest).setStatus(0);
				caps.add(new Point(xTest,yTest));

			}
		}
		board.revalidate();
		board.repaint();
		return caps;
	}

	/**
	 * This method serves to check if a move is actually valid.
	 * 
	 * @param x
	 *          : x portion of the coordinate.
	 * @param y
	 *          : y portion of the move coordinate.
	 * @param move
	 *          : 0 - unoccupied, 1 - white, 2 - black
	 * @param board
	 *          : object which is the focus of our analysis.
	 * @return boolean indicating whether or not this move is valid, true if the
	 *         move is valid (space is unoccupied), false otherwise (space is
	 *         occupied)
	 */
	public static boolean validateMove ( int x, int y, int move, PlayPane pane ) {
		int status = pane.getStatusAt(x,y);
		if ( status == 0 ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method check the current state of the game board for a win. It begins
	 * its check from the specified Point (x,y), which implies that the occupation
	 * status of that Stone will be what we are matching for. This means that if
	 * the coordinate passed into this method has a status of 0, then the method
	 * automatically returns.
	 * 
	 * @param x
	 *          : x portion the specified Stone's coordinate.
	 * @param y
	 *          : y portion of the specified Stone's coordinate.
	 * @param board
	 *          : object that we must check for the win condition.
	 * @return boolean indicating whether or not a win has been reached.
	 */
	public static boolean checkWin ( int x, int y, int capCount, PlayPane pane ) {

		if ( capCount > 4 ) {
			return true;
		}

		/**
		 * Looking for 5 in a row. Can accomplish this using checkAvenue(). This
		 * check is almost identical to checkCombos, except our return value is
		 * boolean and we only return true if we find 5 in a row (don't care about 3
		 * or 4 in a row).
		 */
		int xOffset = 0;
		int yOffset = 0;
		int inARow = 0;

		/*
		 * BEGIN VERTICAL CHECK: offset pair #1: -1, 0 offset pair #2: 1, 0
		 */
		xOffset = -1;
		inARow = PenteUtil.checkAvenue(x,y,xOffset,yOffset,pane);
		if ( inARow > 4 ) return true;

		/*
		 * BEGIN HORIZONTAL CHECK: offset pair #1: 0, 1 offset pair #2: 0, -1
		 */
		xOffset = 0;
		yOffset = 1;
		inARow = PenteUtil.checkAvenue(x,y,xOffset,yOffset,pane);

		if ( inARow > 4 ) return true;

		/*
		 * BEGIN DIAGONAL CHECK #1 (BOTTOM LEFT TO TOP RIGHT) offset pair #1: -1, 1
		 * offset pair #2: 1, -1
		 */
		xOffset = -1;
		yOffset = 1;
		inARow = PenteUtil.checkAvenue(x,y,xOffset,yOffset,pane);
		if ( inARow > 4 ) return true;

		/*
		 * BEGIN DIAGONAL CHECK #2 (BOTTOM LEFT TO TOP RIGHT) offset pair #1: -1, -1
		 * offset pair #2: 1, 1
		 */
		xOffset = -1;
		yOffset = -1;
		inARow = PenteUtil.checkAvenue(x,y,xOffset,yOffset,pane);
		if ( inARow > 4 ) return true;

		return false;
	}

	/**
	 * This method checks a specified Point (x,y) on the Board for combos, both
	 * tesseras and trias. The int value returned must be interpreted bit-wise.
	 * Must analyze each bit to determine what the user should be notified of. 00
	 * - neither 01 - tria 10 - tessera
	 * 
	 * @param x
	 *          : x portion of the Point we are checking.
	 * @param y
	 *          : y portion of the Point we are checking.
	 * @param board
	 *          : game board that holds the Point and Stones.
	 * @return int value corresponding to tessera/tria existence.
	 */
	public static int checkCombos ( int x, int y, PlayPane pane,
	                                Object[] neighbors ) {

		boolean found3 = false; // flag we can set on finding a tria
		int xOffset = 0;
		int yOffset = 0;
		int inARow = 0;

		Object[] keys = neighbors;
		Point holder = null;
		/**
		 * this check, similar to checkWin, is split up into 4 avenues: -horizontal
		 * -vertical -both diagonals
		 */

		for ( int i = 0 ; i < keys.length ; i++ ) {
			inARow = 0;
			holder = new Point((Point) keys[i]);
			xOffset = (int) (holder.getX() - x);
			yOffset = (int) (holder.getY() - y);
			inARow = PenteUtil.checkAvenue(x,y,xOffset,yOffset,pane);

			if ( inARow == 4 ) {
				return 2;
			}
			if ( inARow == 3 ) {
				found3 = true;
			}

		}
		if ( found3 ) {
			return 1;
		}
		return 0;

	}

	/**
	 * Given a Board, Point, and x/y offset, performs continuous checks in the
	 * direction of the offset, returning the number of like pieces detected in a
	 * row.
	 * 
	 * @param x
	 *          - base x.
	 * @param y
	 *          - base y.
	 * @param offsetX
	 *          - add this to x to get the first neighbor we ought to check.
	 * @param offsetY
	 *          - add this to y to get the first neighbor we ought to check.
	 * @param board
	 * @return
	 */
	public static int checkAvenue ( int x, int y, int offsetX, int offsetY,
	                                PlayPane board ) {

		int move = board.getStatusAt(x,y);
		int nextX = x + offsetX;
		int nextY = y + offsetY;
		int total = 1;

		int testMove = board.getStatusAt(nextX,nextY);
		while ( move == testMove ) {
			total++;
			nextX = nextX + offsetX;
			nextY = nextY + offsetY;
			testMove = board.getStatusAt(nextX,nextY);
		}
		if ( offsetX != 0 ) {
			offsetX = -offsetX;
		}
		if ( offsetY != 0 ) {
			offsetY = -offsetY;
		}
		nextX = x + offsetX;
		nextY = y + offsetY;
		testMove = board.getStatusAt(nextX,nextY);
		while ( move == testMove ) {
			total++;
			nextX = nextX + offsetX;
			nextY = nextY + offsetY;
			testMove = board.getStatusAt(nextX,nextY);
		}

		if ( total > 1 ) {
			// System.out.println("Found "+total+" along "+
			// PenteUtil.fetchFamiliar(new Point(x,y),new
			// Point(x+offsetX,y+offsetY)));
		}
		return total;
	}

	/**
	 * This method is purely used for debugging. Given a current P
	 * 
	 * @param cur
	 *          - current Point.
	 * @param rel
	 *          - relative Point.
	 * @return the associated direction in String form of the displacement between
	 *         current and relative. see here for a list of potential return
	 *         values: NE (-1,1) E (0,1) SE (1,1) S (1,0) SW (1,-1) W (0,1) NW
	 *         (-1,-1) N (-1,0) OR null. Returns null in the following cases: -if
	 *         the 2 Points reference the same coordinate. -if the 2 Points aren't
	 *         neighbors.
	 */
	public static String fetchFamiliar ( Point cur, Point rel ) {
		int xCur = (int) cur.getX();
		int yCur = (int) cur.getY();

		int xRel = (int) rel.getX();
		int yRel = (int) rel.getY();

		int xDisplace = xRel - xCur;
		int yDisplace = yRel - yCur;

		if ( xDisplace == -1 && yDisplace == 1 ) {
			return "NE";
		} else if ( xDisplace == 0 && yDisplace == 1 ) {
			return "E";
		} else if ( xDisplace == 1 && yDisplace == 1 ) {
			return "SE";
		} else if ( xDisplace == 1 && yDisplace == 0 ) {
			return "S";
		} else if ( xDisplace == 1 && yDisplace == -1 ) {
			return "SW";
		} else if ( xDisplace == 0 && yDisplace == -1 ) {
			return "W";
		} else if ( xDisplace == -1 && yDisplace == -1 ) {
			return "NW";
		} else if ( xDisplace == -1 && yDisplace == 0 ) {
			return "N";
		} else {
			return null;
		}
	}

	/**
	 * Given the passed in Board and Point(x,y), finds and returns the neighboring
	 * values in a HashMap. To do this, each neighboring spot must be checked. If
	 * one of the neighboring Points doesn't map to anything in Board (outside of
	 * playing surface), then no neighbor is added.
	 * 
	 * @param x
	 * @param y
	 * @param board
	 * @return HashMap<Point,Stone> neighboring Stone values.
	 */
	public static Hashtable<Point,Stone> getNeighbors ( int x, int y,
	                                                    PlayPane board ) {
		Hashtable<Point,Stone> neighbors = new Hashtable<Point,Stone>();
		Point point = new Point(x - 1,y);
		Stone holder = null;

		holder = board.getStoneAt(x - 1,y);
		if ( holder != null ) {
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x - 1,y + 1);
		if ( holder != null ) {
			point.setLocation(point.getX(),point.getY() + 1);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x,y + 1);
		if ( holder != null ) {
			point.setLocation(x,y + 1);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x + 1,y + 1);
		if ( holder != null ) {
			point.setLocation(x + 1,y + 1);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x + 1,y);
		if ( holder != null ) {
			point.setLocation(x + 1,y);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x + 1,y - 1);
		if ( holder != null ) {
			point.setLocation(x + 1,y - 1);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x,y - 1);
		if ( holder != null ) {
			point.setLocation(x,y - 1);
			neighbors.put(point,holder);
		}

		holder = board.getStoneAt(x - 1,y - 1);
		if ( holder != null ) {
			point.setLocation(x - 1,y - 1);
			neighbors.put(point,holder);
		}
		return neighbors;
	}

	/**
	 * This method does the same as the method above, except it only returns
	 * non-empty locations (where status != 0).
	 * 
	 * @param x
	 * @param y
	 * @param board
	 * @return HashMap<Point,Stone> of neighboring occupied locations
	 */
	public static Hashtable<Point,Stone> getNonEmptyNeighbors ( int x, int y,
	                                                            PlayPane board ) {

		/**
		 * holders that get the values of locations that are seen as occupied. Used
		 * for clarity.
		 */
		int xOcc = x - 1;
		int yOcc = y;

		Hashtable<Point,Stone> neighbors = new Hashtable<Point,Stone>();

		Stone holder = null;

		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		yOcc = y + 1;
		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		xOcc = x;

		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		xOcc = x + 1;

		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		yOcc = y;
		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		yOcc = y - 1;
		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		xOcc = x;

		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}

		xOcc = x - 1;

		holder = board.getStoneAt(xOcc,yOcc);
		if ( holder != null && holder.getStatus() != 0 ) {

			neighbors.put(new Point(xOcc,yOcc),holder);
		}
		return neighbors;
	}

}