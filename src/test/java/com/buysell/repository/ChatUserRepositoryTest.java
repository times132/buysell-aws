package com.buysell.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatUserRepositoryTest {
    @Autowired
    private ChatUsersRepository chatUserRepository;

    @AfterEach
    public void cleanup(){
        chatUserRepository.deleteAll();
    }

    @Test
    public void deleteChatUser(){
        chatUserRepository.deleteById(10L);
    }
}
