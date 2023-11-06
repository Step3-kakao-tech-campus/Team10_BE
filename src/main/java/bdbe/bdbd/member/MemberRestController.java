package bdbe.bdbd.member;


import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody @Valid MemberRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        memberService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinUser(@RequestBody @Valid MemberRequest.JoinDTO requestDTO, Errors errors) {
        memberService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new BadRequestError(errorMessage);
        }
        MemberResponse.LoginResponse response = memberService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(null));
    }

    @GetMapping("/info")
    public ResponseEntity<?> findUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        OwnerResponse.UserInfoDTO dto = memberService.findUserInfo(userDetails.getMember());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    // 로그아웃 사용안함 - 프론트에서 JWT 토큰을 브라우저의 localstorage에서 삭제하면 됨.
}
