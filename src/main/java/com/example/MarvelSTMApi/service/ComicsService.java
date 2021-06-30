package com.example.MarvelSTMApi.service;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import com.example.MarvelSTMApi.model.MarvelComics;
import com.example.MarvelSTMApi.repositories.ComicsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ComicsService {
    @Autowired
    private ComicsRepo comicsRepo;
    @Autowired
    CharacterService characterService;

    public Page<MarvelComics> getAllComics(Pageable pageable){
        return comicsRepo.findAll(pageable);
    }

    public MarvelComics getComicsById(String id){
        Optional<MarvelComics> comic = comicsRepo.findById(id);
        if(comic.isEmpty())
            return null;
        else
            return comic.get();
    }

    public Page<MarvelCharacters> getCharactersByComic(MarvelComics id,Pageable pageable) {
        List<MarvelCharacters> characters = id.getCharacters();
        Page<MarvelCharacters> page = new PageImpl<MarvelCharacters>(characters,pageable,characters.size());
        return page;
    }
    public Page<MarvelComics> getComicsByCharacter(MarvelCharacters character,Pageable pageable){
        Page<MarvelComics> page = comicsRepo.findByCharactersContains(character,pageable);
        return page;
    }

    public MarvelComics createComic(MarvelComics comic) {
        MarvelComics newComic = comicsRepo.insert(comic);
        return newComic;
    }

    public void deleteComic (MarvelComics comic) {
        comicsRepo.delete(comic);
    }

    public Page<MarvelComics> getAllComicsWithFilter(Pageable pageable, String filter) {
        return comicsRepo.findBytitleLike(pageable,filter);
    }

    public MarvelComics editComic(MarvelComics id, String title, String writer, String description, Date dateOfPublication, MultipartFile photo,List<String> characters) {
        if(!title.equals(id.getTitle()))
            id.setTitle(title);
        if (!writer.equals(id.getWriter()))
            id.setWriter(writer);
        if(!description.equals(id.getDescription()))
            id.setDescription(description);
        if(dateOfPublication!=id.getDateOfPublication())
            id.setDateOfPublication(dateOfPublication);
        if(photo!=null&& !photo.isEmpty())
            try {
                String imageCode = Base64.getEncoder().encodeToString(photo.getBytes());
                id.setImage(imageCode);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        if(!characters.isEmpty()) {
            ArrayList<MarvelCharacters> currentCharacters = new ArrayList<>();
            for (String idChar : characters
            ) {
                MarvelCharacters character = characterService.getCharacterById(idChar);
                if (character != null)
                    currentCharacters.add(character);
            }
            id.setCharacters(currentCharacters);
        }
        MarvelComics updComic = comicsRepo.save(id);
        return updComic;
    }

    public MarvelComics createComic(String title, String writer, String description, Date dateOfPublication, MultipartFile photo, List<String> characters) {
        ArrayList<MarvelCharacters> currentCharacters = new ArrayList<>();
        if (!characters.isEmpty()) {

            for (String idChar : characters
            ) {
                MarvelCharacters character = characterService.getCharacterById(idChar);
                if (character != null)
                    currentCharacters.add(character);
            }
        }
        String imageCode = "";
        if (photo != null && !photo.isEmpty()) {
        try {
            imageCode = Base64.getEncoder().encodeToString(photo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        MarvelComics newComic = new MarvelComics(title,description,writer,dateOfPublication,currentCharacters,imageCode);
        if (newComic!=null)
            comicsRepo.insert(newComic);
        return newComic;
    }
}
