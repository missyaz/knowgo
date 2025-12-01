package com.fw.know.go.document;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * 验证1：自动缓存命中/未命中
     * 预期结果：
     * 1. 首次调用getUserById：日志打印「缓存未命中，查询DB」
     * 2. 1分钟内再次调用：日志无DB查询，直接返回缓存（JetCache日志打印「hit=true」）
     */
    @Test
    public void testCacheHit() throws InterruptedException {
        log.info("=== 验证缓存命中/未命中 ===");
        String userId = "1001";

        // 第一次调用：缓存未命中（走DB）
        User user1 = userService.getUserById(userId);
        log.info("第一次查询结果：{}", user1);

        // 第二次调用：缓存命中（不走DB）
        User user2 = userService.getUserById(userId);
        log.info("第二次查询结果：{}", user2);

        // 第三次调用：缓存仍命中
        User user3 = userService.getUserById(userId);
        log.info("第三次查询结果：{}", user3);

        // 等待60秒（缓存过期）
        log.info("=== 等待60秒，缓存过期 ===");
        TimeUnit.SECONDS.sleep(60);

        // 第四次调用：缓存过期，再次走DB
        User user4 = userService.getUserById(userId);
        log.info("缓存过期后查询结果：{}", user4);
    }

    /**
     * 验证2：更新后自动失效缓存
     * 预期结果：
     * 1. 先查询用户（缓存命中）
     * 2. 调用updateUser（自动删除缓存）
     * 3. 再次查询：缓存未命中（走DB，获取最新数据）
     */
    @Test
    public void testCacheInvalidate() throws InterruptedException {
        log.info("=== 验证更新后缓存失效 ===");
        String userId = "1002";

        // 1. 首次查询：走DB，缓存写入
        User user1 = userService.getUserById(userId);
        log.info("首次查询结果：{}", user1);

        // 2. 再次查询：缓存命中
        User user2 = userService.getUserById(userId);
        log.info("缓存命中查询结果：{}", user2);

        // 3. 更新用户：自动删除缓存
        userService.updateUser(userId, "更新后的用户名");

        // 4. 再次查询：缓存已失效，走DB（注意：DB数据未真的更新，这里仅验证缓存失效）
        User user3 = userService.getUserById(userId);
        log.info("更新后查询结果（缓存失效）：{}", user3);
    }

    /**
     * 验证3：自动刷新缓存
     * 预期结果：
     * 1. 首次查询：走DB，缓存写入
     * 2. 30秒内查询：缓存命中
     * 3. 30秒后查询：后台自动刷新（日志打印「缓存刷新」），返回新数据
     * 4. 刷新后缓存仍可用，不阻塞查询
     */
    @Test
    public void testCacheAutoRefresh() throws InterruptedException {
        log.info("=== 验证自动刷新缓存 ===");
        String userId = "1003";

        // 1. 首次查询：走DB
        User user1 = userService.getUserWithAutoRefresh(userId);
        log.info("首次查询结果：{}", user1);

        // 2. 10秒后查询：缓存命中（未到刷新时间）
        log.info("=== 等待10秒 ===");
        TimeUnit.SECONDS.sleep(10);
        User user2 = userService.getUserWithAutoRefresh(userId);
        log.info("10秒后查询结果（缓存命中）：{}", user2);

        // 3. 再等25秒（累计35秒，超过刷新时间30秒）
        log.info("=== 再等待25秒（累计35秒，触发自动刷新） ===");
        TimeUnit.SECONDS.sleep(25);
        User user3 = userService.getUserWithAutoRefresh(userId);
        log.info("35秒后查询结果（已刷新）：{}", user3);

        // 4. 再等30秒（累计65秒，再次刷新）
        log.info("=== 再等待30秒（累计65秒，再次触发刷新） ===");
        TimeUnit.SECONDS.sleep(30);
        User user4 = userService.getUserWithAutoRefresh(userId);
        log.info("65秒后查询结果（再次刷新）：{}", user4);
    }

    /**
     * 验证4：缓存统计（命中率）
     * 预期结果：JetCache每1分钟打印统计日志，命中率接近100%
     */
    @Test
    public void testCacheStats() throws InterruptedException {
        log.info("=== 验证缓存命中率 ===");
        String userId = "1004";

        // 多次调用，触发缓存命中
        for (int i = 0; i < 10; i++) {
            userService.getUserById(userId);
            TimeUnit.MILLISECONDS.sleep(50);
        }

        // 等待1分钟，观察JetCache统计日志（命中率≈90%，因为第一次未命中）
        log.info("=== 等待1分钟，观察缓存统计日志 ===");
        TimeUnit.SECONDS.sleep(60);
    }
}