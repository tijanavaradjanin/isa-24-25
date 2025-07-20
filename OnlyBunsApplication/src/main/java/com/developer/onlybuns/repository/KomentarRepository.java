package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Komentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KomentarRepository extends JpaRepository<Komentar, Integer> {
}
