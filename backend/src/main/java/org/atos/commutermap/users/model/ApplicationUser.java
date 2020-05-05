package org.atos.commutermap.users.model;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.common.model.BaseClass;
import org.atos.commutermap.network.service.model.PlainUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Map;

@Entity(name = "ApplicationUser")
@Table(name = "users")
public class ApplicationUser extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public final long userId;
    @Column(name = "email")
    @Nonnull
    public final String email;
    @Nonnull
    @CollectionTable(name = "access_tokens", joinColumns = @JoinColumn(name = "userId"))
    @MapKeyColumn(name = "token_issuer")
    @Column(name = "token_value")
    @ElementCollection(targetClass = String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    public final Map<String, String> accessTokens;

    private ApplicationUser() {
        userId = Long.MIN_VALUE;
        email = null;
        accessTokens = null;
    }

    public ApplicationUser(@Nonnull String email, Map<String, String> accessTokens) {
        this.userId = Long.MIN_VALUE;
        this.email = email;
        this.accessTokens = ImmutableMap.copyOf(accessTokens);
    }

    @Transient
    public org.atos.commutermap.network.service.model.PlainUser toPlainUser() {
        return new PlainUser().email(this.email).id(this.userId);
    }
}
