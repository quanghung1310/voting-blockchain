package com.voting.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="transaction")
public class TransactionDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String transId;
    private String sender;
    private String receipt;
    private Integer value;
    private String currency;
    private Timestamp createDate;
    private String signature;
    private String description;
    private Timestamp lastModify;
    private Integer active;
    private String contentId;
    private Integer isMine;
}
