package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Objava;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KomentarRepository extends JpaRepository<Komentar, Integer> {
}
