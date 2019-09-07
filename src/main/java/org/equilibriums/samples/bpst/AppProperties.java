package org.equilibriums.samples.bpst;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix="bpst")
public class AppProperties {
    private SampleReport sampleReport;

    public SampleReport getSampleReport() {
        return sampleReport;
    }

    public void setSampleReport(SampleReport sampleReport) {
        this.sampleReport = sampleReport;
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
}
