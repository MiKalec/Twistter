package br.com.twistter.model

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
    val name: String? = null,
    @Column(nullable = false, unique = true)
    val login: String? = null,
    @Column(nullable = false)
    var password: String? = null,
    @Transient
    var password2: String? = null,
    @Column(nullable = false)
    val gender: String? = null,
    @Column
    var enabled: Boolean? = null,
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    val creationTime: Date? = null
)