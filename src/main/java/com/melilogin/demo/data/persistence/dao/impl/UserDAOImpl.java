package com.melilogin.demo.data.persistence.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.melilogin.demo.common.persistence.entities.User;
import com.melilogin.demo.data.persistence.dao.impl.base.BaseDAOImpl;
import com.melilogin.demo.data.persistence.dao.interfaces.UserDAO;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class UserDAOImpl extends BaseDAOImpl<User>implements UserDAO {
    private static final long serialVersionUID = 8044043166327091875L;

//    @Override
//    public List<User> findUserToSendInsights(String countryCode) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//
//        Root<User> user = cq.from(User.class);
//        Join<User, UserAccount> userAccount = user.join(User_.userAccount);
//
//        List<Predicate> predicates = new ArrayList<Predicate>();
//
//        predicates.add(cb.equal(user.get(User_.countryCode), countryCode));
//        predicates.add(cb.isNotNull(user.get(User_.domainPurchased)));
//        predicates.add(cb.notEqual(userAccount.get(UserAccount_.subscriptionStatus), SubscriptionStatusType.INITIAL));
//
//        cq.where(predicates.toArray(new Predicate[] {}));
//
//        List<User> resultList = em.createQuery(cq).getResultList();
//        return resultList;
//    }

}
