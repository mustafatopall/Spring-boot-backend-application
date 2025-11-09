package com.mustafatopalearning.spring.repository;

import com.mustafatopalearning.spring.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Post> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> searchPosts(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> searchPostsWithPagination(@Param("keyword") String keyword, Pageable pageable);
}

