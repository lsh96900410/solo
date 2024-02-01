package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext --> Spring DATA JPA 지원
    private final EntityManager em ;

    // 1. 회원 가입
    public void save(Member member){ em.persist(member); }

    // 2. 회원 목록
    public List<Member> findAll(){
        // jpql : 엔티티 대상 쿼리 vs SQL : 테이블 대상 쿼리
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }

    // 3. 회원 조회
    public Member findOne(Long id){ return em.find( Member.class , id); }

    // 4. 회원 이름으로 조회
    public Member findByName(String username){
        try {
            return em.createQuery("select m from Member m where m.username= :username", Member.class)
                    .setParameter("username", username).getSingleResult();
        }catch (NoResultException e ){
            e.printStackTrace();
            return null;
        }
        }

}
