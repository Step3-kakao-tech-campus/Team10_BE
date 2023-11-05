package bdbe.bdbd._core.errors.security;

import bdbe.bdbd.member.MemberResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "jwtTokens")
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Cacheable(key = "#token", condition = "#token != null")
    public String cacheToken(String token) {
        return token;
    }

    @CacheEvict(key = "#token")
    public void evictToken(String token) {
        logger.info("Token evicted from cache: {}", token);
    }

    public MemberResponse.LogoutResponse logout(String token) {
        evictToken(token);
        MemberResponse.LogoutResponse response = new MemberResponse.LogoutResponse();
        response.setSuccess(true);
        return response;
    }}