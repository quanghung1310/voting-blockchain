package com.voting.dto;

import lombok.*;

import javax.persistence.*;
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
    private long id;

    private String transId;

    @Column(length = 2048)
    private String sender;

    @Column(length = 2048)
    private String receiver;

    private int value;

    private String currency;

    private Timestamp createDate;

    @Column(length = 2048)
    private String signature;

    @Column(length = 2048)
    private String description;

    private Timestamp lastModify;

    private int status;


    private String contentId;

    private Integer isMine;
}
