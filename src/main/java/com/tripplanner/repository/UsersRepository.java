package com.tripplanner.repository;
import org.springframework.data.repository.CrudRepository;

import com.tripplanner.entity.UserEntity;
public interface UsersRepository extends CrudRepository<UserEntity,Integer> {
	UserEntity findByEmailId(String emailId);
}
