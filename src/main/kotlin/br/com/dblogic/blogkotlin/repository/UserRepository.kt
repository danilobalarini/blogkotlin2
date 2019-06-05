package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int>, JpaSpecificationExecutor<User> {
}