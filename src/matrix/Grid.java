package matrix;

/**
 * A Class that imitates a two-dimensional array of Objects 
 * @author Noah Kime
 * @param <T> The type of Object to be stored in this Grid
 */
public class Grid<T> {
		
		private int rows;
		private int cols;
		
		private Cell<T> head;
		private Cell<T> last;
		
		////////////////////////////////////////////////////////////
		
		/**
		 * A Class that stores an entry of a given data type and two adjacent Cells,
		 * a Cell to the right and a Cell below in the Grid
		 * @author Noah Kime
		 * @param <V> The given type of data to be stored in this Cell
		 */
		private class Cell<V> {
			
			private V data;
			
			private Cell<V> down;
			private Cell<V> right;
			
			
			/**
			 * Constructs a Cell with no data or adjacent Cells
			 */
			public Cell() {
				this.data = null;
				this.down = null;
				this.right = null;
			} //END Cell (constructor)
			
			
			/*
			 * Constructs a Cell with given data and no adjacent Cells
			 * @param data The given data for this Cell
			 * 
			public Cell(V data) {
				this.data = data;
				this.down = null;
				this.right = null;
			} //END Cell (constructor)
			*/
			
			
			/**
			 * Constructs a Cell with no data and two adjacent Cells
			 * The adjacent cells are to the right and below this Cell
			 * @param down The given adjacent Cell below this Cell
			 * @param right The given adjacent Cell to the right of this Cell
			 */
			public Cell(Cell<V> down, Cell<V> right) {
				this.data = null;
				this.down = down;
				this.right = right;
			} //END Cell (constructor)
			
			
			/* 
			 * Constructs a Cell with given data and two adjacent Cells
			 * The adjacent cells are to the right and below this Cell
			 * @param data The given data for this Cell
			 * @param down The given adjacent Cell below this Cell
			 * @param right The given adjacent Cell to the right of this Cell
			public Cell(V data, Cell<V> down, Cell<V> right) {
				this.data = data;
				this.down = down;
				this.right = right;
			} //END Cell (constructor)
			*/
			
			
			/**
			 * Returns the data in this Cell
			 * @return The data in this Cell
			 */
			public V getData() {
				return data;
			} //END getData
			
			
			/**
			 * Sets the data in this Cell to a given value
			 * @param data The given value for this Cell's data to be set
			 */
			public void set(V data) {
				this.data = data;
			} //END set
			
			
			/**
			 * Returns the adjacent Cell below this Cell
			 * @return The adjacent Cell below this Cell
			 */
			public Cell<V> down() {
				return down;
			} //END down
			
			
			/**
			 * Returns the adjacent Cell to the right of this Cell
			 * @return The adjacent cell to the right of this Cell
			 */
			public Cell<V> right() {
				return right;
			} //END right
			
		} //END Cell
		
		////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a Grid with a given number of rows and columns
		 * @param rows The given number of rows
		 * @param cols The given number of columns
		 */
		public Grid(int rows, int cols) {
			this.rows = rows;
			this.cols = cols;
			
			head = this.setUpGrid();
		} //END Grid (constructor)
		
		
		/**
		 * Returns the value in the Cell in a given row and column
		 * @param row The given row
		 * @param col The given column
		 * @return The value in the Cell in the given row and column
		 */
		public T get(int row, int col) {
			return go(row, col, head).getData();
		} //END get
		
		
		/**
		 * Returns the last Cell referenced
		 * @return The last Cell referenced
		 */
		public T getLast() {
			return last.getData();
		} //END getLast
		
		
		/**
		 * Recursively returns the Cell a given number of positions from a given Cell
		 * @param row The number of rows the desired Cell is down from the given Cell
		 * @param col The number of columns the desired Cell is right from the given Cell
		 * @param here The given Cell to move from
		 * @return The Cell a given number of positions from a given Cell
		 */
		private Cell<T> go(int row, int col, Cell<T> here) {
			if (row > 0)
				return go(row-1, col, here.down());
			else if (col > 0)
				return go(row, col-1, here.right());
			else
				this.last = here;
				return here;
		} //END go
		
		
		/**
		 * Sets the value in a given row and column to a given value
		 * Note that rows and columns begin counting from 0.
		 * @param row The given row position of the value to be set
		 * @param col The given column position of the value to be set
		 * @param val The given value to be set
		 */
		public void set(int row, int col, T val) {
			go(row, col, head).set(val);
		} //END set
		
		
		/**
		 * Returns the number of rows of this Grid
		 * @return The number of rows of this Grid
		 */
		public int getRows() {
			return rows;
		} //END getRows
		
		
		/**
		 * Returns the number of columns of this Grid
		 * @return The number of columns of this Grid
		 */
		public int getCols() {
			return cols;
		} //END getRows
		
		
		/**
		 * Generates and returns this Grid represented as a String
		 * Separates each cell using brackets. Places one blank space between
		 * brackets and contained data. If data is null, prints "null". 
		 * @return the String representation of this Grid
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (this.get(i,j) != null)
						sb.append("[ " + this.get(i,j).toString() + " ]");
					else
						sb.append("[ null ]");
				}
				sb.append("\n");
			}
			
			return sb.toString();
		} //END toString
		
		
		/**
		 * Sets up this Grid starting from the bottom right corner to the top left.
		 * @return The head of this Grid, the top left Cell
		 */
		private Cell<T> setUpGrid() {
			//INPUT: Number of Rows (r) and Columns (c) for this Grid
			//OUTPUT: A complete Grid (Cells bound to right and down neighbors) of r Rows and c Columns
			
			//00 CHECK FOR TRIVIAL INPUT
			//If 1 rows and 1 cols
			//Construct and return cell as head of this Grid
			if (rows == 1 && cols == 1)
				return new Cell<T>();
			
			//01 SET UP ROW ARRAYS
			@SuppressWarnings("unchecked")
			Cell<T>[] crntRow = new Cell[cols];
			@SuppressWarnings("unchecked")
			Cell<T>[] lastRow = new Cell[cols];
			
			//02 CONSTRUCT LAST ROW OF GRID
			//Construct Cell in last column
			//Construct Cells from right to left
			//Set lastRow -> crntRow
			crntRow[0] = new Cell<T>();
			
			for (int c = 1; c < cols; c++) {
				crntRow[c] = new Cell<T>(null, crntRow[c-1]);
			}
			
			lastRow = crntRow;
			
			//03 CONSTRUCT REMAINING ROWS OF GRID FROM BOTTOM TO TOP
			//Construct Cell in last column
			//Construct Cells from right to left
			//Set lastRow -> crntRow
			for (int r = 1; r < rows; r++) {
				crntRow[0] = new Cell<T>(lastRow[0], null);
				
				for (int c = 1; c < cols; c++) {
					crntRow[c] = new Cell<T>(lastRow[c], crntRow[c-1]);
				}
				
				lastRow = crntRow;
			}
			
			//04 RETURN HEAD OF GRID
			//Return last entry of lastRow as head of this Grid
			return lastRow[cols-1];
		} //END setUpGrid
		
	} //END Grid