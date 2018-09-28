package com.sweeter.repos;

import com.sweeter.domain.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface MessageRepo extends CrudRepository<Message, Long>{
    List<Message> findByTag(String tag);
}
