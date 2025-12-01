package com.fw.know.go.document;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.Cached;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    /**
     * 1. 自动缓存查询结果：首次查询走DB，后续走缓存
     * - name：缓存名称（Redis中key前缀为"userCache-"）
     * - key：缓存key（用SpEL表达式，取userId参数）
     * - expire：缓存过期时间（60秒，覆盖本地缓存默认的30秒）
     * - cacheType：缓存级别（LOCAL+REMOTE，默认两级缓存）
     */
    @Cached(name = "userCache-", key = "#userId", expire = 60, timeUnit = TimeUnit.SECONDS)
    public User getUserById(String userId) {
        // 模拟数据库查询（实际开发中替换为MyBatis/Repository调用）
        log.debug("=== 缓存未命中，查询DB获取用户：userId={}", userId);
        // 模拟DB查询延迟（100ms）
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 模拟返回DB数据
        return new User(userId, "默认用户_" + userId, 20);
    }

    /**
     * 2. 更新用户后自动失效缓存：确保缓存与DB数据一致
     * - @CacheInvalidate：删除指定key的缓存
     * - name和key需与@Cached保持一致
     */
    @CacheInvalidate(name = "userCache-", key = "#userId")
    public void updateUser(String userId, String newUsername) {
        // 模拟更新DB
        log.debug("=== 更新DB用户：userId={}, newUsername={}", userId, newUsername);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 3. 自动刷新缓存：缓存未过期但到达刷新时间，后台自动更新缓存
     * - refresh：启用自动刷新
     * - refreshTime：刷新间隔（30秒）
     * - 特点：刷新时不阻塞查询（旧缓存仍可用），后台异步更新
     */
    @Cached(
        name = "userRefreshCache-", 
        key = "#userId", 
        expire = 120, // 缓存总过期时间（120秒）
        timeUnit = TimeUnit.SECONDS
    )
    @CacheRefresh(refresh = 30, timeUnit = TimeUnit.SECONDS)
    public User getUserWithAutoRefresh(String userId) {
        // 模拟DB查询（刷新时会执行此逻辑）
        log.debug("=== 缓存刷新/未命中，查询DB获取用户：userId={}", userId);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 每次刷新返回不同的username（用于验证刷新效果）
        return new User(userId, "刷新用户_" + userId + "_" + System.currentTimeMillis()/1000, 20);
    }
}