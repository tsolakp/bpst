package org.equilibriums.samples.bpst.model;

import java.util.Set;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing a blood sample result.")
@Entity(name="Sample")
public class Sample {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private String patientId;
    
    @ApiModelProperty(notes = "Blood markers taken from the patient's blood")
    @OneToMany(mappedBy="sample", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Marker> markers;
    
    public Sample() {}
    
    public Sample(Long id, String patientId, Set<Marker> markers) {
        this.id = id;
        this.patientId = patientId;
        this.markers = markers;
    }

    public Long getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }    
    
    public Set<Marker> getMarkers() {
        return markers;
    }
    
    @PrePersist
    private void prePersist() {
        markers.forEach( m -> m.sample = this ); 
    }
    
    @Override
    public String toString() {
        return String.format("Sample[id=%d, patientId='%s'']",
            id, patientId);
    }
    
    @ApiModel(description = "Class representing a single marker of blood sample such as red blood count (rbc), white blood count (wbc), platelets, hemoglobin etc.")
    @Entity(name="Sample.Marker")
    public static class Marker {
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        
        @ApiModelProperty(notes = "Marker type such as red blood count (rbc), white blood count (wbc), platelets, hemoglobin etc")
        private String markerType;
        @ApiModelProperty(notes = "Marker level such as 4.32 for red blood count (rbc)")
        private Double markerLevel;
        
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name="sample_id", nullable=false)
        private Sample sample;
        
        public Marker() {}
        
        public Marker(Long id, String markerType, Double markerLevel) {
            this.id = id;
            this.markerType = markerType;
            this.markerLevel = markerLevel;            
        }

        public Long getId() {
            return id;
        }
        
        public Sample getSample() {
            return sample;
        }
        
        public String getMarkerType() {
            return markerType;
        }

        public Double getMarkerLevel() {
            return markerLevel;
        }        
    }

}
