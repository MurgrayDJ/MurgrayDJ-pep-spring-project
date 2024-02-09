package com.example.repository;

import java.util.List;
import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    @Query(value="SELECT * FROM Message WHERE posted_by = :posted_by", nativeQuery = true)
    List<Message> findAllByPostedBy(Integer posted_by);
}
