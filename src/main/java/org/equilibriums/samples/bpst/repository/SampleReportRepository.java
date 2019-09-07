package org.equilibriums.samples.bpst.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.equilibriums.samples.bpst.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class SampleReportRepository {
    private final AppProperties appProperties;
    private final EntityManager entityManager;
    
    @Autowired
    public SampleReportRepository(AppProperties appProperties, EntityManager entityManager) {
        this.appProperties = appProperties;
        this.entityManager = entityManager;
    }
    
    public Long getMatchingMarkerPatientCount(String markerType, double markerLevel) {
        double range = ( ( markerLevel*(double)appProperties.getSampleReport().getSampleMarkerLevelSearchRangePercentage() ) / 100.0 );
        double min = markerLevel - range;
        double max = markerLevel + range;
        
        List<Long> result = entityManager.createQuery(
            "SELECT count(distinct m.sample.patientId) FROM Sample.Marker m WHERE m.markerType = :markerType AND m.markerLevel > :min AND m.markerLevel < :max", Long.class)
            .setParameter("markerType", markerType)
            .setParameter("min", min)
            .setParameter("max", max)
            .getResultList();
        
        return result.isEmpty() ? new Long(0) : result.get(0);
    }
}
