package org.equilibriums.samples.bpst.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing a blood sample that are compared against samples in the database.")
public class SampleReport {
    private Sample sample;
    
    @ApiModelProperty(notes = "The percentage of patient that have similar marker levels for each marker type.")
    private Map<String, Double> matchingMarkerPatientPercentageMap;    
    
    public SampleReport(Sample sample, Map<String, Double> matchingMarkerPatientPercentageMap) {
        this.sample = sample;
        this.matchingMarkerPatientPercentageMap = matchingMarkerPatientPercentageMap;
    }

    public Sample getSample() {
        return sample;
    }

    public Map<String, Double> getMatchingMarkerPatientPercentageMap() {
        return matchingMarkerPatientPercentageMap;
    }
}
