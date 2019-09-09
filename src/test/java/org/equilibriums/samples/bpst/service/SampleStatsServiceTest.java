package org.equilibriums.samples.bpst.service;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.*;

import org.equilibriums.samples.bpst.AppProperties;
import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.model.SampleHistogram;
import org.equilibriums.samples.bpst.repository.SampleRepository;

public class SampleStatsServiceTest extends EasyMockSupport {
    private SampleStatsService sampleStatsService;
    private SampleRepository sampleRepository;
    
    @Before
    public void before() {
        sampleRepository = createMock(SampleRepository.class);
        AppProperties appProperties = new AppProperties();
        AppProperties.SampleStats ss = new AppProperties.SampleStats();
        ss.setMarkerLevelHistogramBucketCount(10);
        appProperties.setSampleStats(ss);
        sampleStatsService = new SampleStatsService(appProperties, sampleRepository);
    }
    
    @Test
    public void testGetSampleHistogramWithNoMathcingMarkers1() {
        SampleHistogram sampleHistogram = getSampleHistogram("m1");
        
        assertEquals( 10, sampleHistogram.getBuckets().size() );
        for ( SampleHistogram.Bucket bucket:sampleHistogram.getBuckets() ) assertEquals( new Integer(0), bucket.getCount() );
    }
    
    @Test
    public void testGetSampleHistogramWithNoMathcingMarkers2() {
        SampleHistogram sampleHistogram = getSampleHistogram( "m1", 
            createSample( createSampleMarker("m2", 10.0) ) );
        
        assertEquals( 10, sampleHistogram.getBuckets().size() );
        for ( SampleHistogram.Bucket bucket:sampleHistogram.getBuckets() ) assertEquals( new Integer(0), bucket.getCount() );
    }
    
    
    @Test
    public void testGetSampleHistogramWithSingleMatchingMarker() {
        SampleHistogram sampleHistogram = getSampleHistogram("m1", 
            createSample( createSampleMarker("m1", 10.0) ) );
        
        List< SampleHistogram.Bucket > buckets = sampleHistogram.getBuckets();
        
        assertEquals( 10, buckets.size() );
        assertBucket( buckets.get(0), 1, 10.0 );
        for ( int i = 1; i < buckets.size(); i++ ) assertEquals( new Integer(0), buckets.get(i).getCount() );
    }
    
    
    @Test
    public void testGetSampleHistogramWithTwoSingleValuesMatchingMarkers() {
        SampleHistogram sampleHistogram = getSampleHistogram("m1", 
            createSample( createSampleMarker("m1", 10.0) ), 
            createSample( createSampleMarker("m1", 10.0), createSampleMarker("m2", 10.0) ) );
        
        List< SampleHistogram.Bucket > buckets = sampleHistogram.getBuckets();
        
        assertEquals( 10, buckets.size() );
        assertBucket( buckets.get(0), 2, 10.0 );
        for ( int i = 1; i < buckets.size(); i++ ) assertEquals( new Integer(0), buckets.get(i).getCount() );
    }
    
    @Test
    public void testGetSampleHistogramWithTwoSingleValuesNonMatchingMarkers() {
        SampleHistogram sampleHistogram = getSampleHistogram("m1", 
            createSample( createSampleMarker("m1", 10.0) ), 
            createSample( createSampleMarker("m1", 11.0), createSampleMarker("m2", 10.0) ) );
        
        List< SampleHistogram.Bucket > buckets = sampleHistogram.getBuckets();
        
        assertEquals( 10, buckets.size() );
        assertBucket( buckets.get(0), 1, 10.0 );
        assertBucket( buckets.get( buckets.size() - 1 ), 1, 11.0 );
        for ( int i = 1; i < buckets.size() - 1; i++ ) assertEquals( new Integer(0), sampleHistogram.getBuckets().get(i).getCount() );
    }
    
    
    @Test
    public void testGetSampleHistogram1() {
        //the bucketSize should be (26-1.0)/10=2.5 
        SampleHistogram sampleHistogram = getSampleHistogram("m1", 
            createSample( createSampleMarker("m1", 10.0) ), 
            createSample( createSampleMarker("m1", 11.0), createSampleMarker("m2", 10.0) ),
            createSample( createSampleMarker("m2", 10.0) ), 
            createSample( createSampleMarker("m1", 12.0) ), 
            createSample( createSampleMarker("m1", 26.0), createSampleMarker("m2", 110.0) ),
            createSample( createSampleMarker("m1", 1.0) ),
            createSample( createSampleMarker("m1", 1.6) ),
            createSample( createSampleMarker("m1", 9.1) ) );
        
        List< SampleHistogram.Bucket > buckets = sampleHistogram.getBuckets();
        
        assertEquals( 10, buckets.size() );
        assertBucket( buckets.get(0), 2, 1.0 );
        assertBucket( buckets.get(1), 0, null );
        assertBucket( buckets.get(2), 0, null );
        assertBucket( buckets.get(3), 2, 9.1 );
        assertBucket( buckets.get(4), 2, 11.0 );
        assertBucket( buckets.get(5), 0, null );
        assertBucket( buckets.get(6), 0, null );
        assertBucket( buckets.get(7), 0, null );
        assertBucket( buckets.get(8), 0, null );
        assertBucket( buckets.get(9), 1, 26.0 );
    }
    
    @Test
    public void testGetSampleHistogram2() {
        SampleHistogram sampleHistogram = getSampleHistogram("m1", 
            createSample( createSampleMarker("m1", 1.0) ), 
            createSample( createSampleMarker("m1", 2.0), createSampleMarker("m2", 10.0) ),
            createSample( createSampleMarker("m2", 10.0) ), 
            createSample( createSampleMarker("m1", 3.0) ), 
            createSample( createSampleMarker("m1", 4.0), createSampleMarker("m2", 110.0) ),
            createSample( createSampleMarker("m1", 5.0) ) );
        
        List< SampleHistogram.Bucket > buckets = sampleHistogram.getBuckets();
        
        assertEquals( 10, buckets.size() );
        assertBucket( buckets.get(0), 1, 1.0 );
        assertBucket( buckets.get(1), 0, null );
        assertBucket( buckets.get(2), 1, 2.0 );
        assertBucket( buckets.get(3), 0, null );
        assertBucket( buckets.get(4), 0, null );
        assertBucket( buckets.get(5), 1, 3.0 );
        assertBucket( buckets.get(6), 0, null );
        assertBucket( buckets.get(7), 1, 4.0 );
        assertBucket( buckets.get(8), 0, null );
        assertBucket( buckets.get(9), 1, 5.0 );
    }
    
    
    private void assertBucket(SampleHistogram.Bucket bucket, int expectedCount, Double expectedValue) {
        assertEquals( new Integer(expectedCount), bucket.getCount() );
        assertEquals( expectedValue, bucket.getValue() );
    }
    
    private SampleHistogram getSampleHistogram(String markerType, Sample... samples) {
        expect( sampleRepository.findAll() ).andReturn( Arrays.asList(samples) );
        replayAll();
        SampleHistogram sampleHistogram = sampleStatsService.getSampleHistogram(markerType);
        verifyAll();
        return sampleHistogram;
    }
    
    private Sample createSample(Sample.Marker... markers) {
        return new Sample(null, null, Arrays.stream(markers).collect( Collectors.toSet() ) );
    }
    
    private Sample.Marker createSampleMarker(String markerType, double markerLevel) {
        return new Sample.Marker(null, markerType, markerLevel);
    }
}
