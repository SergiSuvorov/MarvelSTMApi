package com.example.MarvelSTMApi.repositories;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import com.example.MarvelSTMApi.model.MarvelComics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicsRepo extends MongoRepository<MarvelComics,String> {
    Page<MarvelComics> findByCharactersContains(MarvelCharacters character,Pageable pageable);

    Page<MarvelComics> findBytitleLike(Pageable pageable, String filter);
}
