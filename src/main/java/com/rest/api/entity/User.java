package com.rest.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//Table데이터 매핑을 위한 Model 생성
//Entity는 db table간의 구조와 관계를 JPA요구하는 형태로 만든 model입니다.
//테이블에 있는 컬럼 값들의 정보, 테이블간의 관계정보를 담고있습니다.
//Loombok어노테이션 사용시 소스가 간단해짐

@Builder // builder를 사용할수 있게 합니다.
@Entity  // jpa entity임을 알립니다.
@Getter  // user 필드값의 getter를 자동으로 생성합니다.
@NoArgsConstructor // 인자없는 생성m자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "user") // 'user' 테이블과 매핑됨을 명시
public class User {
    //  pk생성전략을 DB에 위임한다는 의미입니다.
    //  mysql로 보면 pk 필드를 auto_increment로 설정해 놓은 경우로 보면 됩니다.
    @Id // primaryKey임을 알립니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) private long msrl;

    // uid column을 명시합니다. 필수이고 유니크한 필드이며 길이는 30입니다.
    @Column(nullable = false, unique = true, length = 30)
    private String uid;

    // name column을 명시합니다. 필수이고 길이는 100입니다.
    @Column(nullable = false, length = 100)
    private String name;
}

