package jenius.queueservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;

    // 대기열 추가
//    public void addQueue()

    // 대기 순번 확인


    //


    // 대기열 제거


//    private String createEnterToken() {
//
//    }

}
