package com.rest.api.repo;


import com.rest.api.entity.CardCom;
import org.springframework.data.jpa.repository.JpaRepository;

//생성한 entity를 이용하여 Table에 질의를 요청하기 위한 Repository생성
public interface CardJpaRepo extends JpaRepository<CardCom, Long> {
}
