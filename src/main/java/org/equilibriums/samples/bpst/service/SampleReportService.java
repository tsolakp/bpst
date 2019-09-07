package org.equilibriums.samples.bpst.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.model.SampleReport;
import org.equilibriums.samples.bpst.repository.SampleReportRepository;
import org.equilibriums.samples.bpst.repository.SampleRepository;

@Service
public class SampleReportService {
    private final SampleRepository sampleRepository; 
    private final SampleReportRepository sampleReportRepository;
    
    @Autowired
    public SampleReportService(SampleRepository sampleRepository, SampleReportRepository sampleReportRepository) {
        this.sampleRepository = sampleRepository;
        this.sampleReportRepository = sampleReportRepository;
    }
    
    public Optional<SampleReport> getSampleReport(Long sampleId) {
        return sampleRepository.findById(sampleId).map(this::createReport);
    }
    
    private SampleReport createReport(Sample sample) {
        long totalPatientCount = sampleRepository.count();
        Map<String, Double> matchingMarkerPatientPercentageMap = sample.getMarkers().stream().collect( 
            Collectors.toMap(Sample.Marker::getMarkerType, m -> calculateMatchingMarkerPatientPercentage(totalPatientCount, m.getMarkerType(), m.getMarkerLevel() ) ) );
        
        return new SampleReport(sample, matchingMarkerPatientPercentageMap);
    }
    
    private Double calculateMatchingMarkerPatientPercentage(long totalPatientCount, String markerType, double markerLevel) {
        long matchingMarkerPatientCount = sampleReportRepository.getMatchingMarkerPatientCount(markerType, markerLevel);
        double result = ( (double)matchingMarkerPatientCount / (double)totalPatientCount ) * 100.0;
        return result;
    }
}
