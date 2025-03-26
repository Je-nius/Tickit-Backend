package jenius.common.security;

public class JwtConstants {
    public static final Long ACCESS_TOKEN_EXPIRY_HOUR = 1_800_000L; // 30분
    public static final int REFRESH_TOKEN_EXPIRY_DAY = 30;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";
    public static final String AUTHORITY_CLAIMS_NAME = "Auth";
}
