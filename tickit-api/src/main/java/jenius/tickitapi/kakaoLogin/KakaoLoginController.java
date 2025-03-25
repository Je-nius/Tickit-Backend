package jenius.tickitapi.kakaoLogin;

import jenius.userservice.dto.request.KakaoLoginRequestDto;
import jenius.userservice.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "/api/kakao/login")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping
    public void login() {
        kakaoLoginService.authorize();
    }

    @GetMapping("/authorize")
    public void authorize(@RequestBody KakaoLoginRequestDto loginRequestDto) {
        kakaoLoginService.getToken(loginRequestDto);
    }

}
