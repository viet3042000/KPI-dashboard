package com.b4t.app.commons;


import com.b4t.app.service.dto.ClusterDTO;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.ManhattanDistance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KmeanCluster {
    private static final int NUM_ITERATOR = 100;

    private static final Logger logger = LoggerFactory.getLogger(KmeanCluster.class);

    public static void main(String[] args) {
        Double[] data = new Double[]{
            32.0, 25.0, -9.0, 27.0, 21.0, 27.0, 21.0, 40.0, 16.0, 31.0, 25.0,
            6.0, 24.0, -3.0, 27.0, 2.0, 30.0, -8.0, 38.0, 23.0, 30.0, 39.0, -7.0, 14.0, 7.0, 28.0, 5.0,
            -5.0, -2.0, 20.0, 28.0, -2.0, 17.0, 5.0, 11.0, -3.0, 5.0, 23.0, -7.0, -6.0, 39.0, 40.0, 40.0,
            19.0, 37.0, 39.0, -10.0, 10.0, 38.0, 1.0, 24.0, -5.0, 33.0, -1.0, 29.0, 25.0, 23.0, -1.0, 10.0,
            18.0, 23.0, 39.0, 22.0, 0.0
        };
        KmeanCluster kmeanCluster = new KmeanCluster();
        List<ClusterDTO> lstClus = kmeanCluster.getCluster(Arrays.asList(data), 5);
        System.out.println(lstClus.size());
    }

    public List<ClusterDTO> getCluster(List<Double> data, int numOfCluster){
        String dataStr = StringUtils.join(data,",");
        logger.info("Danh sach du lieu truyen vao:" + dataStr);
        logger.info("So luong khoang chia:" + numOfCluster);
        List<ClusterDTO> lstCluster = new ArrayList<>();
        if (data.size() == 1) {
            ClusterDTO clusterDTO = new ClusterDTO(0, data.get(0));
            lstCluster.add(clusterDTO);
        } else if (data.size() > 1 && data.size() <= numOfCluster) {
            ClusterDTO clusterDTO = new ClusterDTO(data.get(0), data.get(data.size() - 1));
            lstCluster.add(clusterDTO);
        } else if (data.size() > 1 && data.size() < numOfCluster * 2) {
            lstCluster = doGetCluster(data, numOfCluster / 2, lstCluster);
        } else {
            lstCluster = doGetCluster(data, numOfCluster, lstCluster);
        }
        return lstCluster;
    }

    private List<ClusterDTO> doGetCluster(List<Double> data, int numOfCluster, List<ClusterDTO> lstCluster) {
        Dataset dataset = new DefaultDataset();
        data.stream().forEach(e -> {
            dataset.add(new DenseInstance(new double[]{e}, e));
        });
        if (dataset.size() == 0) {
            return null;
        }
        Clusterer kmean = new KMeans(numOfCluster, NUM_ITERATOR, new ManhattanDistance());
        Dataset[] cluster = kmean.cluster(dataset);
        for (Dataset clus : cluster) {
            if(!clus.isEmpty()) {
                lstCluster.add(getCluster(clus.iterator()));
            }
        }
        //Set lai gia tri max de cac cluster co gia tri max-min lien tuc
        lstCluster.sort(Comparator.comparing(ClusterDTO::getMinValue).reversed());
        try {
            Iterator<ClusterDTO> iterator = lstCluster.iterator();
            ClusterDTO maxCluster = iterator.next().clone();
            while (iterator.hasNext()) {
                ClusterDTO current = iterator.next();
                current.setMaxValue(maxCluster.getMinValue());
                maxCluster = current.clone();
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }
        for(ClusterDTO clusterDTO: lstCluster) {
            logger.info("- Khoang:" + clusterDTO.getMinValue() + " - " + clusterDTO.getMaxValue());
        }
        return lstCluster;
    }

    public ClusterDTO getCluster(Iterator<Instance> data) {
        Double max = null, min = null;
        while (data.hasNext()) {
            Instance instance = data.next();
            max = (max == null || max.compareTo(instance.get(0)) < 0) ? instance.get(0) : max;
            min = (min == null || min.compareTo(instance.get(0)) > 0) ? instance.get(0) : min;
        }
        return new ClusterDTO(min, max);
    }
}
