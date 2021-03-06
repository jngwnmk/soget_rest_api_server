package soget.repository;

import java.util.ArrayList;

import soget.model.Bookmark;
import soget.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String>{
	public User findByUserId(String userId);
	public ArrayList<User> findByUserIdLike(String keyword);
	
}
