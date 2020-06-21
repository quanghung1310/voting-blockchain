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
    @Column(length = 2048)
    private String privateKey;
    @Column(length = 2048)
    private String publicKey;
    private String email;
    private String password;
    private int active;
    private Timestamp createDate;
    private Timestamp lastModify;
    private String firstName;
    private String lastName;
    private int sex;
    private int type;
    private int maxPerDate;
}
