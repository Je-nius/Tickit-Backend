package jenius.userservice.service;

import jenius.common.security.JwtGenerator;
import jenius.userservice.dto.request.LoginRequestDto;
import jenius.userservice.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;

    public TokenResponse authenticate(LoginRequestDto loginRequestDto) {

        // 비밀번호 검증
        // 따로 비밀번호 match() 할 필요 없이, AuthenticationManager 가 자동으로 비밀번호 검증을 수행한다.
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getLoginId());

        String accessToken = jwtGenerator.generateAccessToken(authenticate);
        String refreshToken = jwtGenerator.generateRefreshToken(authenticate);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
