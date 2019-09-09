package org.equilibriums.samples.bpst.model;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class SampleHistogram {
    private static Bucket emptyBucket = new Bucket(0, null);
    
    private List<Bucket> buckets;
    
    public SampleHistogram(List<Bucket> buckets) {
        this.buckets = buckets;
    }
    
    public List<Bucket> getBuckets() {
        return buckets;
    }

    public static class Bucket {
        private Integer count;
        private Double value;
        
        public Bucket(Integer count, Double value) {
            this.count = count;
            this.value = value;
        }

        public Integer getCount() {
            return count;
        }

        public Double getValue() {
            return value;
        }
    }
    
    public static SampleHistogramBuilder builder(int bucketCount) {
        return new SampleHistogramBuilder(bucketCount);
    }
    
    public static class SampleHistogramBuilder {
        private final List<Bucket> buckets; 
                
        private SampleHistogramBuilder(int bucketCount) {
            buckets = IntStream.range(0, bucketCount).mapToObj(i -> emptyBucket).collect( Collectors.toList() );
        }
        
        public SampleHistogramBuilder updateBucket(int index, Integer countIncrement, Double value) {
            Bucket bucket = buckets.get(index);
            if (bucket == emptyBucket) buckets.set( index, new Bucket(countIncrement, value) );
            else bucket.count += countIncrement;
            return this;
        }
        
        public SampleHistogram build() {
            return new SampleHistogram(buckets);
        }
    }
}
