package soget.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import soget.model.MarkIn;

public interface MarkInRepository extends MongoRepository<MarkIn,String>{
	
	   Page<MarkIn> findByUserKeyIdInAndBookmarkIdNotInAndDateLessThan(Iterable<String> friends_key_ids, Iterable<String> exclusives_bookmark_ids, long date, Pageable pageable);
		   Page<MarkIn> findByUserKeyIdInAndBookmarkIdNotInAndDateLessThanAndPrivacyIsFalse(Iterable<String> friends_key_ids, Iterable<String> exclusives_bookmark_ids, long date, Pageable pageable);
	   Page<MarkIn> findByUserKeyId(String user_key_id, Pageable pageable);
	   Page<MarkIn> findByUserKeyIdAndPrivacyIsFalse(String user_key_id, Pageable pageable);
}
