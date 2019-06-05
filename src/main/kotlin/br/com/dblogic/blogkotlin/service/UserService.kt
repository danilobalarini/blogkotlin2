package br.com.dblogic.blogkotlin.service

import br.com.dblogic.blogkotlin.model.User
import br.com.dblogic.blogkotlin.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
	
	@Autowired
	lateinit var userRepository: UserRepository

		fun findAll(): List<User> {
		return userRepository.findAll()
	}
	
	fun save(user: User) : User {
		return userRepository.save(user)
	}

}