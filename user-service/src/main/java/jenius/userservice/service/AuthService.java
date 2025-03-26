package jenius.userservice.service;

import jenius.common.exception.CustomException;
import jenius.common.security.JwtGenerator;
import jenius.userservice.dto.request.LoginRequestDto;
import jenius.userservice.dto.response.TokenResponse;
import jenius.userservice.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;

    public TokenResponse authenticate(LoginRequestDto loginRequestDto) {

        // 비밀번호 검증
        // 따로 비밀번호 match() 할 필요 없이, AuthenticationManager 가 자동으로 비밀번호 검증을 수행한다.
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getLoginId());

            String accessToken = jwtGenerator.generateAccessToken(authenticate);
            String refreshToken = jwtGenerator.generateRefreshToken(authenticate);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            log.error("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
            throw new CustomException(UserErrorCode.UNAUTHORIZED_USER);
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage());
            throw new CustomException(UserErrorCode.LOGIN_ERROR);
        }
    }


}
