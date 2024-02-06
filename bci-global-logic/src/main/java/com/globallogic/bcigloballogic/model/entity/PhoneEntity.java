package com.globallogic.bcigloballogic.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "phone")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @Column(name = "id", nullable = false)
    private UUID id;

    private Long number;
    private Integer citycode;
    private String countrycode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
