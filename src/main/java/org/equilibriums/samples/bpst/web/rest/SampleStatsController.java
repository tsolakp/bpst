package org.equilibriums.samples.bpst.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.equilibriums.samples.bpst.model.SampleHistogram;
import org.equilibriums.samples.bpst.service.SampleStatsService;

@RestController
@RequestMapping("/sample-stats")
@Api("Provides histogram distribution of sample marker level across patients.")
public class SampleStatsController {
    private final SampleStatsService sampleStatsService;
    
    @Autowired
    public SampleStatsController(SampleStatsService sampleStatsService) {
        this.sampleStatsService = sampleStatsService;
    }
    
    @RequestMapping(method = GET, value="/{markerType}", produces = "application/json")
    @ApiOperation("Returns histogram distribution of sample marker level across patients. Example markerTypes - rbc, wbc, platelets, hemoglobin")
    public SampleHistogram getSampleHistogram(@PathVariable String markerType) {
        return sampleStatsService.getSampleHistogram(markerType);
    }
}
