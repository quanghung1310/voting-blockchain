package com.voting.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="elector")
public class ElectorDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String walletId;

    private String contentId;
}
