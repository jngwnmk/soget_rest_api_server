package soget.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import soget.model.Invitation;

public interface InvitationRepository extends MongoRepository<Invitation,String>{
	   public Invitation findByInvitationNum(String invitation);
	   

}
