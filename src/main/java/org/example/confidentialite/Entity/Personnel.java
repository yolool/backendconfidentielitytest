package org.example.confidentialite.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personnel", indexes = {
        @Index(name = "idx_dept_id", columnList = "department, idPersonnel")
})
public class Personnel {
    @Id
    private String IdPersonnel;
    private String Name;
    private String Department;
    @CurrentTimestamp
    private LocalDateTime date;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
