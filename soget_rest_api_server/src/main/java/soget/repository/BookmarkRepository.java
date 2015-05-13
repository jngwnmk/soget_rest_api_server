package soget.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import soget.model.Bookmark;

public interface BookmarkRepository extends MongoRepository<Bookmark,String>{
	 Page<Bookmark> findByInitUserId(Iterable<String> user_ids, Pageable pageable);
	 Page<Bookmark> findByInitUserId(String user_id, Pageable pageable);
	 
	 Page<Bookmark> findByInitUserIdIn(Iterable<String> user_ids, Pageable pageable);
	 Page<Bookmark> findByInitUserIdInAndDateLessThan(Iterable<String> user_ids, long date, Pageable pageable);
	 Page<Bookmark> findByDateLessThanAndInitUserIdIn(long date,Iterable<String> user_ids, Pageable pageable);
	 Page<Bookmark> findByInitUserIdInAndIdNotInAndDateLessThan(Iterable<String> user_ids, Iterable<String> exclusives_ids, long date, Pageable pageable);
	 Page<Bookmark> findByDateLessThan(Date date, Pageable pageable);
	 
}
