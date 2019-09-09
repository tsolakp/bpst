package org.equilibriums.samples.bpst.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.equilibriums.samples.bpst.AppProperties;
import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.model.SampleHistogram;
import org.equilibriums.samples.bpst.repository.SampleRepository;

@Service
@Transactional
public class SampleStatsService {
    private final AppProperties appProperties;
    private final SampleRepository sampleRepository; 
    
    @Autowired
    public SampleStatsService(AppProperties appProperties, SampleRepository sampleRepository) {
        this.appProperties = appProperties;
        this.sampleRepository = sampleRepository;
    }
    
    public SampleHistogram getSampleHistogram(String markerType) {
        List<Sample> samples = sampleRepository.findAll();
        
        List<Double> sampleLevels = samples.parallelStream()
            .flatMap( s -> s.getMarkers().stream() )
            .filter( m -> m.getMarkerType().equals(markerType) )
            .map( sm -> sm.getMarkerLevel() )
            .sorted()
            .collect( Collectors.toList() );
        
        SampleHistogram.SampleHistogramBuilder shb = SampleHistogram.builder( appProperties.getSampleStats().getMarkerLevelHistogramBucketCount() );
        
        if ( sampleLevels.isEmpty() ) return shb.build();
                
        double min = sampleLevels.get(0);
        double max = sampleLevels.get( sampleLevels.size() - 1 );
        
        double bucketSize = (max - min) / (double)appProperties.getSampleStats().getMarkerLevelHistogramBucketCount();
                
        for (double sampleLevel : sampleLevels) {
            int index = (int)( (sampleLevel - min) / bucketSize );
            if ( index == appProperties.getSampleStats().getMarkerLevelHistogramBucketCount() ) index -= 1;
            shb.updateBucket(index, 1, sampleLevel);
        }
        
        return shb.build();
    }
}
