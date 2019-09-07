package org.equilibriums.samples.bpst.repository;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.equilibriums.samples.bpst.Application;
import org.equilibriums.samples.bpst.model.Sample;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses={Application.class})
public class SampleReportRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;
    
    @Autowired
    private SampleReportRepository sampleReportRepository;
    
    @Test
    public void testWithEmpty() {
        assertEquals( new Long(0), sampleReportRepository.getMatchingMarkerPatientCount("m1", 10) );
    }
    
    @Test
    public void testWithSingleNonMatching() {
        sampleRepository.save( createSample("p1", createSampleMarker("m1", 9.0) ) );
        assertEquals( new Long(0), sampleReportRepository.getMatchingMarkerPatientCount("m1", 10.0) );
        assertEquals( new Long(0), sampleReportRepository.getMatchingMarkerPatientCount("m2", 10.0) );
    }
    
    @Test
    public void testWithSingleMatching() {
        sampleRepository.save( createSample("p1", createSampleMarker("m1", 9.8) ) );
        assertEquals( new Long(1), sampleReportRepository.getMatchingMarkerPatientCount("m1", 10.0) );
        assertEquals( new Long(0), sampleReportRepository.getMatchingMarkerPatientCount("m2", 10.0) );
    }
    
    @Test
    public void test() {
        sampleRepository.save( createSample("p1", createSampleMarker("m1", 9.8), createSampleMarker("m2", 7.8) ) );
        sampleRepository.save( createSample("p1", createSampleMarker("m1", 9.8), createSampleMarker("m2", 7.9) ) );
        sampleRepository.save( createSample("p2", createSampleMarker("m1", 2.0) ) );
        sampleRepository.save( createSample("p3", createSampleMarker("m1", 9.8) ) );
        sampleRepository.save( createSample("p3", createSampleMarker("m1", 9.7) ) );
        sampleRepository.save( createSample("p4", createSampleMarker("m1", 19.8) ) );
        sampleRepository.save( createSample("p5", createSampleMarker("m1", 10.2), createSampleMarker("m2", 10.1) ) );
        
        System.out.println( sampleRepository.findAll() );
        
        assertEquals( new Long(3), sampleReportRepository.getMatchingMarkerPatientCount("m1", 10.0) ); //p1,p3,p5
        assertEquals( new Long(1), sampleReportRepository.getMatchingMarkerPatientCount("m2", 7.5) ); //p1
        assertEquals( new Long(1), sampleReportRepository.getMatchingMarkerPatientCount("m2", 10.0) ); //p5
    }
    
    private Sample createSample(String patientId, Sample.Marker... markers) {
        return new Sample(null, patientId, Arrays.stream(markers).collect( Collectors.toSet() ) );
    }
    
    private Sample.Marker createSampleMarker(String markerType, double markerLevel) {
        return new Sample.Marker(null, markerType, markerLevel);
    }
}
