package org.equilibriums.samples.bpst.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.equilibriums.samples.bpst.model.Sample;
import org.equilibriums.samples.bpst.repository.SampleRepository;

@Service
@Transactional
public class SampleService {
    private final SampleRepository sampleRepository; 
    
    @Autowired
    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }
    
    public Sample processSample(Sample sample) {
        return sampleRepository.save(sample);
    }
    
    public Page<Sample> getSamples(Pageable pageable) {
        return sampleRepository.findAll(pageable);
    }
    
    public Optional<Sample> getSample(Long sampleId) {
        return sampleRepository.findById(sampleId);
    }
    
    public Optional<Sample> deleteSample(Long sampleId) {
        Optional<Sample> result = sampleRepository.findById(sampleId);
        result.ifPresent( s -> sampleRepository.delete(s) );
        return result;
    }
}
