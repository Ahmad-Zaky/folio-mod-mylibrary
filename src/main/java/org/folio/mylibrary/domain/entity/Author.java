package org.folio.mylibrary.domain.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "created_by_user_id")
    private UUID createdByUserId;
  
    @Column(name = "updated_by_user_id")
    private UUID updatedByUserId;
  
    public void merge(Author another) {
        this.name = another.getName();
    }
}
