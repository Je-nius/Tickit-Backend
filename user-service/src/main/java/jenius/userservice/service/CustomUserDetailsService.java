package jenius.userservice.service;

import jenius.common.exception.CustomException;
import jenius.userservice.entity.CustomUserDetails;
import jenius.userservice.entity.User;
import jenius.userservice.exception.UserErrorCode;
import jenius.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다."));

        return new CustomUserDetails(user);
    }
}
