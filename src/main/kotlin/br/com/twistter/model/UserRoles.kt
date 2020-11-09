package br.com.twistter.model

import br.com.twistter.model.enums.RoleType
import javax.persistence.*

@Entity
@Table(name = "user_roles")
data class UserRoles(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var user: User? = null,

    @Column
    @Enumerated(EnumType.STRING)
    private var role: RoleType? = null,

    @Column(nullable = false)
    var login: String? = null

)
