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
@Entity(name="block")
public class BlockDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String blockId;
    private String previousHash;
    private String hash;
    private Long nonce;
    private String transId;
    private int difficulty;
    private String minerId;
    private Integer parentId;
    private Integer isActive;
    private Integer statusBlock;
    private Timestamp lastModify;
    private Timestamp createDate;
    private String timeHash;
    private Integer total;
}
