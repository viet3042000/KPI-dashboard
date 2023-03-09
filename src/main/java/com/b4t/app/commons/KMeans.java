package com.b4t.app.commons;


import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.DatasetTools;

import java.security.SecureRandom;
import java.util.Random;

public class KMeans implements Clusterer {
    /**
     * The number of clusters.
     */
    private int numberOfClusters = 4;

    /**
     * The number of iterations the algorithm should make. If this value is
     * Integer.INFINITY, then the algorithm runs until the centroids no longer
     * change.
     */
    private int numberOfIterations = -1;

    /**
     * Random generator for this clusterer.
     */
    private SecureRandom rg;

    /**
     * The distance measure used in the algorithm, defaults to Euclidean
     * distance.
     */
    private DistanceMeasure dm;

    /**
     * The centroids of the different clusters.
     */
    private Instance[] centroids;

    /**
     * Constuct a default K-means clusterer with 100 iterations, 4 clusters, a
     * default random generator and using the Euclidean distance.
     */
    public KMeans() {
        this(4);
    }

    /**
     * Constuct a default K-means clusterer with the specified number of
     * clusters, 100 iterations, a default random generator and using the
     * Euclidean distance.
     *
     * @param k the number of clusters to create
     */
    public KMeans(int k) {
        this(k, 100);
    }

    /**
     * Create a new Simple K-means clusterer with the given number of clusters
     * and iterations. The internal random generator is a new one based upon the
     * current system time. For the distance we use the Euclidean n-space
     * distance.
     *
     * @param clusters   the number of clusters
     * @param iterations the number of iterations
     */
    public KMeans(int clusters, int iterations) {
        this(clusters, iterations, new EuclideanDistance());
    }

    /**
     * Create a new K-means clusterer with the given number of clusters and
     * iterations. Also the Random Generator for the clusterer is given as
     * parameter.
     *
     * @param clusters   the number of clustesr
     * @param iterations the number of iterations
     * @param dm         the distance measure to use
     */
    public KMeans(int clusters, int iterations, DistanceMeasure dm) {
        this.numberOfClusters = clusters != 0 ? clusters : 4;
        this.numberOfIterations = iterations;
        this.dm = dm;
        rg = new SecureRandom();
        rg.setSeed(100);
    }

    /**
     * Execute the KMeans clustering algorithm on the data set that is provided.
     *
     * @param data data set to cluster
     */
    public Dataset[] cluster(Dataset data) {
        // Place K points into the space represented by the objects that are
        // being clustered. These points represent the initial group of
        // centroids.
        // DatasetTools.
        Instance min = DatasetTools.minAttributes(data); Instance max = DatasetTools.maxAttributes(data);
        this.centroids = new Instance[numberOfClusters];
        int instanceLength = data.instance(0).noAttributes();
        for (int j = 0; j < numberOfClusters; j++) {
            double[] randomInstance = DatasetTools.getRandomInstance(data, rg);
            this.centroids[j] = new DenseInstance(randomInstance);
        }

        int iterationCount = 0;
        boolean centroidsChanged = true, randomCentroids = true;
        while (randomCentroids || (iterationCount < this.numberOfIterations && centroidsChanged)) {
            iterationCount++;
            // Assign each object to the group that has the closest centroid.
            int[] assignment = new int[data.size()];
            for (int i = 0; i < data.size(); i++) {
                int tmpCluster = 0;
                double minDistance = dm.measure(centroids[0], data.instance(i));
                for (int j = 1; j < centroids.length; j++) {
                    double dist = dm.measure(centroids[j], data.instance(i));
                    if (dm.compare(dist, minDistance)) {
                        minDistance = dist; tmpCluster = j;
                    }
                }
                assignment[i] = tmpCluster;
            }
            // When all objects have been assigned, recalculate the positions of
            // the K centroids and start over.
            // The new position of the centroid is the weighted center of the
            // current cluster.
            double[][] sumPosition = new double[this.numberOfClusters][instanceLength];
            int[] countPosition = new int[this.numberOfClusters];
            for (int i = 0; i < data.size(); i++) {
                Instance in = data.instance(i);
                for (int j = 0; j < instanceLength; j++) {
                    sumPosition[assignment[i]][j] += in.value(j);
                }
                countPosition[assignment[i]]++;
            }
            centroidsChanged = false; randomCentroids = false;
            for (int i = 0; i < this.numberOfClusters; i++) {
                if (countPosition[i] > 0) {
                    double[] tmp = new double[instanceLength];
                    for (int j = 0; j < instanceLength; j++) {
                        tmp[j] = (float) sumPosition[i][j] / countPosition[i];
                    }
                    Instance newCentroid = new DenseInstance(tmp);
                    if (dm.measure(newCentroid, centroids[i]) > 0.0001) {
                        centroidsChanged = true; centroids[i] = newCentroid;
                    }


                } else {
                    double[] randomInstance = new double[instanceLength];
                    for (int j = 0; j < instanceLength; j++) {
                        double dist = Math.abs(max.value(j) - min.value(j));
                        randomInstance[j] = (float) (min.value(j) + rg.nextDouble() * dist);
                    }
                    randomCentroids = true;
                    this.centroids[i] = new DenseInstance(randomInstance);
                }

                //Chi duyet khoang 10k vong lap de chay tinh toan lai vi tri trung tam
                if (iterationCount >= 10000) {
                    centroidsChanged = false; randomCentroids = false;
                }
            }
        }
        Dataset[] output = new Dataset[centroids.length];
        for (int i = 0; i < centroids.length; i++)
            output[i] = new DefaultDataset();
        for (int i = 0; i < data.size(); i++) {
            int tmpCluster = 0;
            double minDistance = dm.measure(centroids[0], data.instance(i));
            for (int j = 0; j < centroids.length; j++) {
                double dist = dm.measure(centroids[j], data.instance(i));
                if (dm.compare(dist, minDistance)) {
                    minDistance = dist; tmpCluster = j;
                }
            }
            output[tmpCluster].add(data.instance(i));
        }
        return output;
    }

}
