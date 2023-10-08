package bdbe.bdbd.user;


import bdbe.bdbd._core.errors.exception.Exception400;
import bdbe.bdbd._core.errors.exception.Exception500;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd.region.RegionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            userJPARepository.save(requestDTO.toEntity(encodedPassword));
        } catch (Exception e) {
            throw new Exception500("unknown server error");
        }
    }

//    public String login(UserRequest.LoginDTO requestDTO) {
//        User userPS = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
//                () -> new Exception400("이메일을 찾을 수 없습니다 : "+requestDTO.getEmail())
//        );
//
//        if(!passwordEncoder.matches(requestDTO.getPassword(), userPS.getPassword())){
//            throw new Exception400("패스워드가 잘못입력되었습니다.");
//        }
//        return JWTProvider.create(userPS);
//    }

    public UserResponse.LoginResponse login(UserRequest.LoginDTO requestDTO) {
        User userPS = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new Exception400("이메일을 찾을 수 없습니다 : "+requestDTO.getEmail())
        );

        if(!passwordEncoder.matches(requestDTO.getPassword(), userPS.getPassword())) {
            throw new Exception400("패스워드가 잘못입력되었습니다.");
        }

        String jwt = JWTProvider.create(userPS);
        String redirectUrl = "/user/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<User> userOP = userJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new Exception400("동일한 이메일이 존재합니다 : " + email);
        }
    }

}
