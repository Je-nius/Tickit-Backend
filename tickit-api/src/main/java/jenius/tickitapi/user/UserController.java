package jenius.tickitapi.user;

import jenius.userservice.dto.request.UserCreateRequestDto;
import jenius.userservice.dto.response.UserCreateResponseDto;
import jenius.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final String ID_VERIFY_KEY = "available";

    @PostMapping("/signup")
    public ResponseEntity<UserCreateResponseDto> join(@RequestBody UserCreateRequestDto createRequestDto) {
        return ResponseEntity.ok(userService.createUser(createRequestDto));
    }

    @PostMapping("/verify/id")
    public ResponseEntity<Map<String, Boolean>> verifyLoginId(String loginId) {
        return ResponseEntity.ok(Map.of(ID_VERIFY_KEY, userService.verifyLoginId(loginId)));
    }

    @DeleteMapping("/delete")
    public void deleteUser(String password) {
        userService.deleteUser(password);
    }

}
