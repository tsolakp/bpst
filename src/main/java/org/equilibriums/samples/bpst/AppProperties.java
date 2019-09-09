package org.equilibriums.samples.bpst;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix="bpst")
public class AppProperties {
    private SampleReport sampleReport;
    private SampleStats sampleStats;

    public SampleReport getSampleReport() {
        return sampleReport;
    }

    public void setSampleReport(SampleReport sampleReport) {
        this.sampleReport = sampleReport;
    }
    
    
    public SampleStats getSampleStats() {
        return sampleStats;
    }

    public void setSampleStats(SampleStats sampleStats) {
        this.sampleStats = sampleStats;
    }

    public static class SampleReport {
        private Integer sampleMarkerLevelSearchRangePercentage;

        public Integer getSampleMarkerLevelSearchRangePercentage() {
            return sampleMarkerLevelSearchRangePercentage;
        }

        public void setSampleMarkerLevelSearchRangePercentage(Integer sampleMarkerLevelSearchRangePercentage) {
            this.sampleMarkerLevelSearchRangePercentage = sampleMarkerLevelSearchRangePercentage;
        }
    }
    
    public static class SampleStats {
        private Integer markerLevelHistogramBucketCount;

        public Integer getMarkerLevelHistogramBucketCount() {
            return markerLevelHistogramBucketCount;
        }

        public void setMarkerLevelHistogramBucketCount(Integer markerLevelHistogramBucketCount) {
            this.markerLevelHistogramBucketCount = markerLevelHistogramBucketCount;
        }
    }
}
