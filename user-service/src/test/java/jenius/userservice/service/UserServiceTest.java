package jenius.userservice.service;

import jenius.userservice.dto.request.UserCreateRequestDto;
import jenius.userservice.dto.response.UserCreateResponseDto;
import jenius.userservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void join_success() {

        // given
        String loginId = "test1234";
        String username = "테스트";

        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .loginId(loginId)
                .password("test1234")
                .username(username)
                .email("test1234@example.com")
                .birth(LocalDate.of(2000, 7, 20))
                .phoneNumber("010-1234-1234")
                .build();

        // when
        UserCreateResponseDto responseDto = userService.createUser(createRequestDto);

        // then
        Assertions.assertThat(responseDto.getId()).isNotNull();
        Assertions.assertThat(responseDto.getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(responseDto.getUsername()).isEqualTo(username);
    }

}