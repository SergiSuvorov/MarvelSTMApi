package com.example.MarvelSTMApi.controller;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import com.example.MarvelSTMApi.model.MarvelComics;
import com.example.MarvelSTMApi.service.CharacterService;
import com.example.MarvelSTMApi.service.ComicsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/characters",produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "transaction",description = "Операции с персонажами")
public class CharactersController {
    @Autowired
    private CharacterService characterService;
    @Autowired
    private ComicsService comicsService;

    @ApiOperation(value = "Получить всех персонажей")
    @GetMapping ()
    public ResponseEntity<Page<MarvelCharacters>> returnAllCharacters(@RequestParam(required = false,defaultValue = "") String filter,
                                @PageableDefault Pageable pageable){
        Page<MarvelCharacters> page = null;
        if (filter!=null && !filter.isEmpty()){
             page = characterService.getAllCharactersByFilter(pageable,filter);
        }
        else
            page = characterService.getAllCharacters(pageable);
        if (page==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(page,HttpStatus.OK);
    }

    @ApiOperation("Получить персонажа по id")
    @GetMapping("{characterId}")
    public ResponseEntity<MarvelCharacters> getById(@PathVariable("characterId")  String characterId){
        MarvelCharacters character = characterService.getCharacterById(characterId);

        if (character==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);;
        return new ResponseEntity<>(character, HttpStatus.OK);
    }

    @ApiOperation("Получить все комиксы персонажа")
    @GetMapping("{characterId}/comics")
    public ResponseEntity<Page<MarvelComics>> getComicsById(@PathVariable("characterId") String characterId, @PageableDefault Pageable pageable)
    {
        MarvelCharacters character = characterService.getCharacterById(characterId);
        if(character==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Page<MarvelComics> comicsList = null;
        comicsList = comicsService.getComicsByCharacter(character,pageable);
        if (comicsList==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(comicsList,HttpStatus.OK);
    }

    @ApiOperation("добавить нового персонажа")
    @PostMapping
    public ResponseEntity<MarvelCharacters> createNewCharacter(@RequestParam String realName, @RequestParam String fullName,
                                                               @RequestParam String description, @RequestParam(required = false, name ="photo") MultipartFile photo){
        MarvelCharacters createdCharacter = characterService.createCharacter(fullName,realName,description,photo);
        if(createdCharacter==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(createdCharacter,HttpStatus.CREATED);
    }

    @ApiOperation("изенить данные текущего персонажа")
    @PutMapping("{characterId}")
    public ResponseEntity<MarvelCharacters> updateCharacter(@PathVariable("characterId") String id,
                                                            @RequestParam String description,@RequestParam String fullName,
                                                            @RequestParam(required = false, name ="photo") MultipartFile photo,
                                                            @RequestParam String realName){
        MarvelCharacters curCharacter = characterService.getCharacterById(id);
        if(curCharacter==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        MarvelCharacters character = characterService.editCharacter(curCharacter,description,fullName,photo,realName);
        if (character==null){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(character,HttpStatus.OK);
    }

    @ApiOperation("Удалить персонажа по id")
    @DeleteMapping("{characterId}")
    public ResponseEntity<MarvelCharacters> deleteCharacter(@PathVariable("characterId") String characterId){
        MarvelCharacters character = characterService.getCharacterById(characterId);
        if(character==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        characterService.removeCharacter(character);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
