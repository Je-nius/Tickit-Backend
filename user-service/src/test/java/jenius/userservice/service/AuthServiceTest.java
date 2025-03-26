package jenius.userservice.service;

import jenius.common.exception.CustomException;
import jenius.userservice.dto.request.LoginRequestDto;
import jenius.userservice.dto.request.UserCreateRequestDto;
import jenius.userservice.dto.response.TokenResponse;
import jenius.userservice.dto.response.UserCreateResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("로그인할 수 있다.")
    void authenticate() {
        // given
        String loginId = "test1234";
        String password = "test1234";

        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .loginId(loginId)
                .password(password)
                .username("테스트")
                .email("test1234@example.com")
                .birth(LocalDate.of(2000, 7, 20))
                .phoneNumber("010-1234-1234")
                .build();

        UserCreateResponseDto createResponseDto = userService.createUser(createRequestDto);

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId(loginId)
                .password(password)
                .build();

        // when
        TokenResponse tokenResponse = authService.authenticate(loginRequestDto);

        // then
        Assertions.assertThat(tokenResponse.getAccessToken()).isNotNull();
        Assertions.assertThat(tokenResponse.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("비밀번호가 다르면 로그인에 실패한다.")
    void authenticate_fail_incorrect_password() {
        // given
        String loginId = "test1234";
        String password = "test1234";

        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .loginId(loginId)
                .password(password)
                .username("테스트")
                .email("test1234@example.com")
                .birth(LocalDate.of(2000, 7, 20))
                .phoneNumber("010-1234-1234")
                .build();

        UserCreateResponseDto createResponseDto = userService.createUser(createRequestDto);

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId(loginId)
                .password("test1111")
                .build();

        // when & then
        assertThrows(CustomException.class, () -> authService.authenticate(loginRequestDto));
    }
}