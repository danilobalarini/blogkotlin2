package br.com.dblogic.blogkotlin.repository

import br.com.dblogic.blogkotlin.model.DateAudit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.time.Instant

@NoRepositoryBean
interface DateAuditRepository : JpaRepository<DateAudit, Instant> {

	fun findbyTop6OrderedByCreatedAt(): List<DateAudit>

}