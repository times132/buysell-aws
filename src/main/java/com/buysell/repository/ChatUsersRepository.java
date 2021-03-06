package com.buysell.repository;

import com.buysell.domain.entity.ChatUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatUsersRepository extends JpaRepository<ChatUsers, Long> {

    List<ChatUsers> findAllByUserNickname(String nickname);
    @Modifying
    @Transactional
    @Query(value = "delete from chatusers where cid=?1", nativeQuery=true)
    void deleteUserById(Long cid);
}
