package bdbe.bdbd.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class MemberRequest {
    @Getter
    @Setter
    public static class JoinDTO {

        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;

        @Size(min = 2, max = 20, message = "2에서 20자 이내여야 합니다.")
        @NotEmpty
        private String username;

        @Size(min = 9, max = 14)
        @NotEmpty
        private String tel;

        public Member toUserEntity(String encodedPassword) {
            return Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .role(MemberRole.ROLE_USER)
                    .tel(tel)
                    .build();
        }

        public Member toOwnerEntity(String encodedPassword) {
            return Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .role(MemberRole.ROLE_OWNER)
                    .tel(tel)
                    .build();
        }

    }


    @Getter
    @Setter
    public static class LoginDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class EmailCheckDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;
    }

    @Getter
    @Setter
    public static class UpdatePasswordDTO {
        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;
    }

}


