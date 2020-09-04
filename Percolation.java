/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    final private int CLOSED = 0;
    final private int OPEN = 1;

    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int openSitesCount = 0;
    private int gridWidth;
    private int[] openSites;
    private int virtualTop;
    private int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        gridWidth = n;
        int totalSites = (n * n);
        weightedQuickUnionUF = new WeightedQuickUnionUF(totalSites + 2);
        openSites = new int[totalSites];
        for (int i = 0; i < totalSites; i++) {
            openSites[i] = CLOSED;
        }
        // Create virtual top and virtual bottoms
        virtualTop = totalSites;
        virtualBottom = totalSites + 1;
        for (int i = 0; i < n; i++) {
            weightedQuickUnionUF.union(virtualTop, i);
            weightedQuickUnionUF.union(virtualBottom, totalSites - 1 - i);
        }
    }

    private boolean isOutOfBoundary(int row, int col) {
        return row < 1 || row > gridWidth || col < 1 || col > gridWidth;
    }

    private int xyTo1D(int row, int col) {
        return (col - 1) + (gridWidth * (row - 1));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOutOfBoundary(row, col)) throw new IllegalArgumentException();
        int gridIndex = xyTo1D(row, col);
        if (!isOpen(row, col)) {
            openSites[gridIndex] = OPEN;
            openSitesCount++;
            if (col < gridWidth && isOpen(row, col + 1)) {
                weightedQuickUnionUF.union(gridIndex, gridIndex + 1);
            }
            if (row > 1 && isOpen(row - 1, col)) {
                weightedQuickUnionUF.union(gridIndex, gridIndex - gridWidth);
            }
            if (col > 1 && isOpen(row, col - 1)) {
                weightedQuickUnionUF.union(gridIndex, gridIndex - 1);
            }
            if (row < gridWidth && isOpen(row + 1, col)) {
                weightedQuickUnionUF.union(gridIndex, gridIndex + gridWidth);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (isOutOfBoundary(row, col)) throw new IllegalArgumentException();
        int gridIndex = xyTo1D(row, col);
        return openSites[gridIndex] == OPEN;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (isOutOfBoundary(row, col)) throw new IllegalArgumentException();
        if (isOpen(row, col)) {
            int gridIndex = xyTo1D(row, col);
            int virtualTopCanonicalElement = weightedQuickUnionUF.find(virtualTop);
            int canonicalElement = weightedQuickUnionUF.find(gridIndex);
            return virtualTopCanonicalElement == canonicalElement;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        int virtualTopCanonicalElement = weightedQuickUnionUF.find(virtualTop);
        int virtualBottomCanonicalElement = weightedQuickUnionUF.find(virtualBottom);
        return virtualTopCanonicalElement == virtualBottomCanonicalElement;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(3, 3);
        perc.open(2, 3);
        perc.open(1, 3);
        perc.isFull(3, 3);
        perc.percolates();
    }
}
