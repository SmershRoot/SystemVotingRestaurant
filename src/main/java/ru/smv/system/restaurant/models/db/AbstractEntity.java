package ru.smv.system.restaurant.models.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATE_DATE", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "MODIFIED_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modifiedDate;
}
