package com.ssafy.realrealfinal.userms.db.repository;

import com.ssafy.realrealfinal.userms.db.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
