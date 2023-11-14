-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- user Table Create SQL
-- 테이블 생성 SQL - user
CREATE TABLE user
(
    `user_id`        INT             NOT NULL    AUTO_INCREMENT COMMENT '유저 식별자', 
    `solvedac_id`    VARCHAR(50)     NULL        COMMENT 'solvedac 아이디. unique', 
    `github_id`      VARCHAR(50)     NOT NULL    COMMENT 'github 아이디', 
    `profile_image`  VARCHAR(255)    NOT NULL    COMMENT '프로필 이미지', 
    `url`            VARCHAR(500)    NULL        COMMENT '홍보 url', 
    `provider_id`    INT             NULL        COMMENT '깃허브 제공 고유식별자', 
     PRIMARY KEY (user_id)
);

-- 테이블 Comment 설정 SQL - user
ALTER TABLE user COMMENT '유저 테이블';

-- Unique Index 설정 SQL - user(solvedac_id)
CREATE UNIQUE INDEX UQ_user_1
    ON user(solvedac_id);


-- whitelist Table Create SQL
-- 테이블 생성 SQL - whitelist
CREATE TABLE whitelist
(
    `whitelist_id`  INT             NOT NULL    AUTO_INCREMENT COMMENT '기본키', 
    `domain`        VARCHAR(100)    NOT NULL    DEFAULT 'unique' COMMENT '도메인', 
     PRIMARY KEY (whitelist_id)
);

-- 테이블 Comment 설정 SQL - whitelist
ALTER TABLE whitelist COMMENT '도메인 화이트리스트';

-- Unique Index 설정 SQL - whitelist(domain)
CREATE UNIQUE INDEX UQ_whitelist_1
    ON whitelist(domain);


-- board Table Create SQL
-- 테이블 생성 SQL - board
CREATE TABLE board
(
    `board_id`  INT           NOT NULL    AUTO_INCREMENT COMMENT '기본키', 
    `user_id`   INT           NOT NULL    COMMENT '유저 아이디', 
    `type`      TINYINT(3)    NOT NULL    COMMENT '타입. 0 : 건의사항 1: whitelist 2:개발자칭찬', 
    `content`   TEXT          NOT NULL    COMMENT '건의 내용', 
     PRIMARY KEY (board_id)
);

-- 테이블 Comment 설정 SQL - board
ALTER TABLE board COMMENT '건의사항';

-- Foreign Key 설정 SQL - board(user_id) -> user(user_id)
ALTER TABLE board
    ADD CONSTRAINT FK_board_user_id_user_user_id FOREIGN KEY (user_id)
        REFERENCES user (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - board(user_id)
-- ALTER TABLE board
-- DROP FOREIGN KEY FK_board_user_id_user_user_id;