package jenius.userservice.dto.response;

import jenius.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateResponseDto {
    private Long id;
    private String loginId;
    private String username;

    public static UserCreateResponseDto fromEntity(User user) {
        return UserCreateResponseDto.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .build();
    }

}
