package dev.n7meless.repository;

import dev.n7meless.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getPostsByUserIdOrderByCreatedDt(long id);
}
