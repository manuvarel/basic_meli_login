package com.melilogin.demo.common.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@DynamicUpdate
@Table(name = "user")
//@NamedQueries({
//        @NamedQuery(name = User.LIST_SUCCESS_DELEGATIONS, query = "SELECT u FROM User u WHERE status = 'success'") })
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2623350100041284473L;

    public static final String LIST_SUCCESS_DELEGATIONS = "Usercontact.listSuccessDelegations";

    @Id
    @Column(name = "meli_user_id")
    private Long meliUserId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "access_token")
    private String accessToken;

}
