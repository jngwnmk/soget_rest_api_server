package soget.repository;

import soget.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
	public User findByUserId(String userId);
	
}
