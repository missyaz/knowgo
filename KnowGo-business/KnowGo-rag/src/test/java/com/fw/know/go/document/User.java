package com.fw.know.go.document;

import lombok.Data;
import java.io.Serializable; // 分布式缓存需序列化

@Data
public class User implements Serializable { // 注意：Redis缓存需实现Serializable
    private String userId;
    private String username;
    private Integer age;

    public User(String userId, String username, Integer age) {
        this.userId = userId;
        this.username = username;
        this.age = age;
    }
}