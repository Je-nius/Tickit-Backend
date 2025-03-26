package jenius.userservice.service;

import jenius.common.exception.CustomException;
import jenius.userservice.dto.request.UserCreateRequestDto;
import jenius.userservice.dto.response.UserCreateResponseDto;
import jenius.userservice.entity.CustomUserDetails;
import jenius.userservice.entity.User;
import jenius.userservice.entity.UserRole;
import jenius.userservice.exception.UserErrorCode;
import jenius.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreateResponseDto createUser(UserCreateRequestDto createRequestDto) {

        String encodedPassword = passwordEncoder.encode(createRequestDto.getPassword());

        User user = User.builder()
                .loginId(createRequestDto.getLoginId())
                .password(encodedPassword)
                .username(createRequestDto.getUsername())
                .email(createRequestDto.getEmail())
                .birth(createRequestDto.getBirth())
                .phoneNumber(createRequestDto.getPhoneNumber())
                .role(UserRole.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        return UserCreateResponseDto.fromEntity(savedUser);
    }

    public Boolean verifyLoginId(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }

    public void deleteUser(String password) {

        String loginId = getCurrentLoginId();

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER_BY_LOGIN_ID));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(UserErrorCode.UNAUTHORIZED_USER);
        }

        userRepository.delete(user);
    }

    private String getCurrentLoginId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername();
        }
        throw new CustomException(UserErrorCode.NOT_LOGIN);
    }

    /**
     * TODO: findId
     */

    /**
     * TODO: changePassword
     */

    /**
     * TODO: mypage
     */


}
