package org.equilibriums.samples.bpst.web.rest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.service.SampleService;

@RestController
@RequestMapping("/sample")
@Api("CRUD operation to manage patient blood samples.")
public class SampleController {
    private final SampleService sampleService;
    
    @Autowired
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }
    
    @RequestMapping(method = POST)
    @ApiOperation("Adds blood sample into the database. Returns added sample with the unique id assigned to it in the database")
    public ResponseEntity<Sample> createSample(@RequestBody Sample sample) {
        return ResponseEntity.status(HttpStatus.CREATED).body( sampleService.processSample(sample) );
    }
    
    @RequestMapping(method = GET, produces = "application/json")
    @ApiOperation("Returns all blood samples from the database. Supports pagination")
    public Page<Sample> getSamples(Pageable pageable) {
        return sampleService.getSamples(pageable);
    }
    
    @RequestMapping(method = GET, value="/{sampleId}", produces = "application/json")
    @ApiOperation("Returns blood sample by specified sampleId. Example sampleIds - 4, 9, 14, 19, 24, 29, 34, 39, 44, 49")
    public Sample getSample(@PathVariable Long sampleId) {
        return sampleService.getSample(sampleId).orElseThrow( () ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find sample for - " + sampleId) );
    }
    
    @RequestMapping(method = DELETE, value="/{sampleId}")
    @ApiOperation("Deletes blood sample from database by specified sampleId. Example sampleIds - 4, 9, 14, 19, 24, 29, 34, 39, 44, 49\"")
    public void deleteSample(@PathVariable Long sampleId) {
        sampleService.deleteSample(sampleId).orElseThrow( () ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find sample for - " + sampleId) );
    }
}
