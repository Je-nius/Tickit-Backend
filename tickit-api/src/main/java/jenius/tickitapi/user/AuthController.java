package jenius.tickitapi.user;

import jenius.userservice.dto.request.LoginRequestDto;
import jenius.userservice.dto.response.TokenResponse;
import jenius.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok()
                .body(authService.authenticate(loginDto));
    }

}
