package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

	User findUserByEmail(String email);

}
