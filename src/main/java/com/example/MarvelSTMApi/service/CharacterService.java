package com.example.MarvelSTMApi.service;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import com.example.MarvelSTMApi.repositories.CharactersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class CharacterService {
    @Autowired
    private CharactersRepo charactersRepo;

    public Page<MarvelCharacters> getAllCharacters(Pageable page){
        return charactersRepo.findAll(page);
    }
    public MarvelCharacters getCharacterById(String id){
        Optional<MarvelCharacters> character = charactersRepo.findById(id);
        if (character.isEmpty())
            return null;
        return character.get();
    }

    public Page<MarvelCharacters> getAllCharactersByFilter(Pageable pageable, String filter) {
        return charactersRepo.findByfullNameLike(pageable,filter);
    }

    public void removeCharacter(MarvelCharacters characterId) {
        charactersRepo.delete(characterId);
    }

    public MarvelCharacters createCharacter(String fullName, String realName, String description, MultipartFile photo)  {
        MarvelCharacters character = null;
        try {
            String imageCode = Base64.getEncoder().encodeToString(photo.getBytes());
            character = new MarvelCharacters(fullName,realName,description,imageCode);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if (character!=null)
            charactersRepo.insert(character);
        return character;
    }

    public MarvelCharacters editCharacter(MarvelCharacters character, String description, String fullName, MultipartFile photo, String realName) {
        if(!description.equals(character.getDescription()))
            character.setDescription(description);
        if(!fullName.equals(character.getFullName()))
            character.setFullName(fullName);
        if(!realName.equals(character.getRealName()))
            character.setRealName(realName);
        if(photo!=null){
            try {
                String imageCode = Base64.getEncoder().encodeToString(photo.getBytes());
                character.setPhoto(imageCode);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        MarvelCharacters updCharacter = charactersRepo.save(character);
        return updCharacter;
    }
}
