package com.example.MarvelSTMApi.repositories;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharactersRepo extends MongoRepository<MarvelCharacters,String> {
    Page<MarvelCharacters> findByfullNameLike(Pageable pageable, String filter);
}
