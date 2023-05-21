package com.roy.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roy.blog.models.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
