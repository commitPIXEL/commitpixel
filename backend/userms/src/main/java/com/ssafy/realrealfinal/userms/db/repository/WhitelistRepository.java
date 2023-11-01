package com.ssafy.realrealfinal.userms.db.repository;

import com.ssafy.realrealfinal.userms.db.entity.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhitelistRepository extends JpaRepository<Whitelist, Integer> {

    Whitelist findByUrlContaining(String host);
}
