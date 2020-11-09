package br.com.twistter.model

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "followers")
class Follower(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val relationId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var user: User? = null,

    @Column(nullable = false)
    var followerId: Long? = null,

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    var creationTime: Date? = null

)
