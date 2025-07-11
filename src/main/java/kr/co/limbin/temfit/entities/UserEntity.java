package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "email")
public class UserEntity {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birth;
    private String gender;
    private String contactMvnoCode;
    private String contactFirst;
    private String contactSecond;
    private String contactThird;
    private String addressPostal;
    private String addressPrimary;
    private String addressSecondary;
    private boolean isAdmin;
    private boolean isSeller;
    private boolean isDeleted;
    private boolean isSuspended;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}