package com.ssafy.realrealfinal.userms.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "whitelist")
@Entity
public class Whitelist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "whitelist_id")
    private Integer id;

    @Column(name = "domain", unique = true)
    private String url;

    @Builder
    public Whitelist(String url) {
        this.url = url;
    }
}
