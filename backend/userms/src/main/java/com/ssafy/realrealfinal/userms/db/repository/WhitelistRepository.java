package com.ssafy.realrealfinal.userms.db.repository;

import com.ssafy.realrealfinal.userms.db.entity.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WhitelistRepository extends JpaRepository<Whitelist, Integer> {

    // WHERE:url의 url은 매개변수 url
    // CONCAT(domain, '%')의 domain은 DB whitelist table의 컬럼 domain
    @Query(value = "SELECT * FROM whitelist WHERE :url LIKE CONCAT(domain, '%') LIMIT 1", nativeQuery = true)
    Whitelist findFirstByUserUrl(String url);

}
