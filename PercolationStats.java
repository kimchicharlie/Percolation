/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialsResults;
    private double T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        int sitesNumber = n * n;
        T = (double) trials;
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        trialsResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                openRandomSite(perc, n);
            }
            trialsResults[i] = (double) perc.numberOfOpenSites() / sitesNumber;
        }
    }

    private void openRandomSite(Percolation perc, int n) {
        int randomRow = StdRandom.uniform(n) + 1;
        int randomColumn = StdRandom.uniform(n) + 1;
        if (perc.isOpen(randomRow, randomColumn)) {
            openRandomSite(perc, n);
        }
        else {
            perc.open(randomRow, randomColumn);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialsResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double sampleMean = mean();
        double sampleStandardDeviation = stddev();
        return sampleMean - ((1.96 * sampleStandardDeviation) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double sampleMean = mean();
        double sampleStandardDeviation = stddev();
        return sampleMean + ((1.96 * sampleStandardDeviation) / Math.sqrt(T));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        double sampleMean = percStats.mean();
        double sampleStddev = percStats.stddev();
        double sampleConLo = percStats.confidenceLo();
        double sampleConHi = percStats.confidenceHi();
        StdOut.println("mean\t\t\t\t\t= " + sampleMean);
        StdOut.println("stddev\t\t\t\t\t= " + sampleStddev);
        StdOut.println(
                "95% confidence interval\t= [" + sampleConLo + ", " + sampleConHi + "]");
    }
}
