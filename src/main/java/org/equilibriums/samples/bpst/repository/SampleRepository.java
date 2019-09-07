package org.equilibriums.samples.bpst.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.equilibriums.samples.bpst.model.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
