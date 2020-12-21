package br.com.twistter.model

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false, unique = true)
    var login: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @Transient
    var password2: String? = null,

    @Column(nullable = false)
    var gender: String? = null,

    @Column
    var enabled: Boolean? = null,

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    val creationTime: Date? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var userRoles: Set<UserRoles?>? = null,

    @Column(nullable = true)
    @ColumnDefault("0")
    var tweetCount: Int? = 0,

    @Column(nullable = true)
    @ColumnDefault("0")
    var followerCount: Int? = 0,

    @Column(nullable = true)
    @ColumnDefault("0")
    var followingCount: Int? = 0,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userTweets: Set<Tweet?>? = null,

    @Transient
    var alreadyFollows: Boolean? = false,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userFollowers: Set<Follower?>? = null
)