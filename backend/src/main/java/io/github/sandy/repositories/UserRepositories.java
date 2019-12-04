package io.github.sandy.repositories;

import io.github.sandy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository<User,Long> {
}
