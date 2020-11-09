package br.com.twistter.repository

import br.com.twistter.model.UserRoles
import org.springframework.data.repository.CrudRepository

interface UserRolesRepository : CrudRepository<UserRoles, Long> {
}