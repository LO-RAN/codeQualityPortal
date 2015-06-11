package com.compuware.toolbox.util.math;

/**
 *
 * @author cwfr-dzysman
 */
public class Deviation {

    /** Method for computing deviation of double values*/
    public static double deviation(Double[] x) {
        double mean = mean(x);
        double squareSum = 0;

        for (int i = 0; i < x.length; i++) {
            squareSum += Math.pow(x[i] - mean, 2);
        }

        return Math.sqrt((squareSum) / (x.length));
    }

    /** Method for computing mean of an array of double values*/
    public static double mean(Double[] x) {
        double sum = 0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }

        return sum / x.length;
    }

    /** Method for computing deviation of double values*/
    public static double deviation(double[] x) {
        double mean = mean(x);
        double squareSum = 0;

        for (int i = 0; i < x.length; i++) {
            squareSum += Math.pow(x[i] - mean, 2);
        }

        return Math.sqrt((squareSum) / (x.length - 1));
    }

    /** Method for computing deviation of int values*/
    public static double deviation(int[] x) {
        double mean = mean(x);
        double squareSum = 0;

        for (int i = 0; i < x.length; i++) {
            squareSum += Math.pow(x[i] - mean, 2);
        }

        return Math.sqrt((squareSum) / (x.length - 1));
    }

    /** Method for computing mean of an array of double values*/
    public static double mean(double[] x) {
        double sum = 0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }

        return sum / x.length;
    }

    /** Method for computing mean of an array of int values*/
    public static double mean(int[] x) {
        int sum = 0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }

        return sum / x.length;
    }
}
