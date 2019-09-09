package org.equilibriums.samples.bpst.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.equilibriums.samples.bpst.model.SampleReport;
import org.equilibriums.samples.bpst.service.SampleReportService;

@RestController
@RequestMapping("/sample-report")
@Api("Manages report for a given blood sample.")
public class SampleReportController {
    private final SampleReportService sampleReportService;
    
    @Autowired
    public SampleReportController(SampleReportService sampleReportService) {
        this.sampleReportService = sampleReportService;
    }
    
    @RequestMapping(method = GET, value="/{sampleId}", produces = "application/json")
    @ApiOperation("Returns blood sample report with comparision against existing patient samples. Example sampleIds - 4, 9, 14, 19, 24, 29, 34, 39, 44, 49")
    public SampleReport getSample(@PathVariable Long sampleId) {
        return sampleReportService.getSampleReport(sampleId).orElseThrow( () ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find sample for - " + sampleId) );
    }
}
