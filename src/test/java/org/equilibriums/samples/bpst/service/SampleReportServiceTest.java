package org.equilibriums.samples.bpst.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.*;

import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.model.SampleReport;
import org.equilibriums.samples.bpst.repository.SampleRepository;
import org.equilibriums.samples.bpst.repository.SampleReportRepository;

public class SampleReportServiceTest extends EasyMockSupport {

    private SampleReportService sampleReportService;
    private SampleRepository sampleRepository;
    private SampleReportRepository sampleReportRepository;
    
    @Before
    public void before() {
        sampleRepository = createMock(SampleRepository.class);
        sampleReportRepository = createMock(SampleReportRepository.class);
        sampleReportService = new SampleReportService(sampleRepository, sampleReportRepository);
    }
    
    @Test
    public void testGetSampleReportWithNoResult() {
        expect( sampleRepository.findById(1L) ).andReturn( Optional.empty() );
        replayAll();
        assertFalse( sampleReportService.getSampleReport(1L).isPresent() );
        verifyAll();
    }
    
    @Test
    public void testGetSampleReport1() {
        SampleReport sampleReport = getSampleReport( 2, 
            new SampleMarkerExpectation( 1L, createSampleMarker("m1") ) );
        
        assertEquals( new Double(50.0), sampleReport.getMatchingMarkerPatientPercentageMap().get("m1") );
    }
    
    @Test
    public void testGetSampleReport2() {
        SampleReport sampleReport = getSampleReport( 1, 
            new SampleMarkerExpectation( 1L, createSampleMarker("m1") ), 
            new SampleMarkerExpectation( 1L, createSampleMarker("m2") ) );
        
        assertEquals( new Double(100.0), sampleReport.getMatchingMarkerPatientPercentageMap().get("m1") );
        assertEquals( new Double(100.0), sampleReport.getMatchingMarkerPatientPercentageMap().get("m2") );
    }
    
    @Test
    public void testGetSampleReport3() {
        SampleReport sampleReport = getSampleReport( 2, 
            new SampleMarkerExpectation( 1L, createSampleMarker("m1") ), 
            new SampleMarkerExpectation( 2L, createSampleMarker("m2") ) );
        
        assertEquals( new Double(50.0), sampleReport.getMatchingMarkerPatientPercentageMap().get("m1") );
        assertEquals( new Double(100.0), sampleReport.getMatchingMarkerPatientPercentageMap().get("m2") );
    }
    
    public SampleReport getSampleReport(long totalPatientCount, SampleMarkerExpectation... sampleMarkerExpectations) {
        expect( sampleRepository.count() ).andReturn(totalPatientCount);
        
        Sample sample = new Sample(1L, "p1", Arrays.stream(sampleMarkerExpectations).map(SampleMarkerExpectation::getMarker).collect( Collectors.toSet() ) );
        expect( sampleRepository.findById(1L) ).andReturn( Optional.of(sample) );
        
        for (SampleMarkerExpectation sampleMarkerExpectation : sampleMarkerExpectations) expect( sampleReportRepository.getMatchingMarkerPatientCount( 
            sampleMarkerExpectation.getMarker().getMarkerType(), sampleMarkerExpectation.getMarker().getMarkerLevel() ) ).andReturn( sampleMarkerExpectation.getExpectedMatchingMarkerPatientCount() );
        
        replayAll();
        SampleReport sampleReport = sampleReportService.getSampleReport(1L).get();
        verifyAll();
        
        assertSame( sample, sampleReport.getSample() );
        
        return sampleReport;
    }
    
    private Sample.Marker createSampleMarker(String markerType) {
        return new Sample.Marker(1L, markerType, 10.0);
    }
    
    private static class SampleMarkerExpectation {
        private Sample.Marker marker;
        private long expectedMatchingMarkerPatientCount;
        
        public SampleMarkerExpectation(long expectedMatchingMarkerPatientCount, Sample.Marker marker) {
            this.expectedMatchingMarkerPatientCount = expectedMatchingMarkerPatientCount;
            this.marker = marker;
        }

        public Sample.Marker getMarker() {
            return marker;
        }

        public long getExpectedMatchingMarkerPatientCount() {
            return expectedMatchingMarkerPatientCount;
        }        
    }
}
