package com.roy.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roy.blog.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
