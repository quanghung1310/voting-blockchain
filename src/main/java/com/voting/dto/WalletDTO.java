package com.voting.dto;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="wallet")
public class WalletDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String walletId;
    private String privateKey;
    private String publicKey;
    private String email;
    private String password;
    private int active;
    private Timestamp createDate;
    private Timestamp lastModify;
}
