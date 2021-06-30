package com.example.MarvelSTMApi.controller;

import com.example.MarvelSTMApi.model.MarvelCharacters;
import com.example.MarvelSTMApi.model.MarvelComics;
import com.example.MarvelSTMApi.service.ComicsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController

@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "transaction",description = "Операции с комиксами")
public class ComicsController {
    @Autowired
    private ComicsService comicsService;

    @ApiOperation("получить все комиксы")
    @GetMapping("/comics")
    public ResponseEntity<Page<MarvelComics>> findAllComics(@RequestParam(required = false,defaultValue = "") String filter,
                                            @PageableDefault Pageable pageable){
        Page<MarvelComics> page =null;
        if(filter!=null&!filter.isEmpty())
            page = comicsService.getAllComicsWithFilter(pageable,filter);
        else
            page = comicsService.getAllComics(pageable);
        if (page ==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(page,HttpStatus.OK);
    }

    @ApiOperation("получить комикс по id")
    @GetMapping("/comics/{id}")
    public ResponseEntity<MarvelComics> findComicsById(@PathVariable("id") String id){
        MarvelComics comic = comicsService.getComicsById(id);
        if(comic==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(comic,HttpStatus.OK);
    }

    @ApiOperation("получить всех персонажей комикса")
    @GetMapping("/comics/{id}/characters")
    public ResponseEntity<Page<MarvelCharacters>>findCharactersByComic(@PathVariable("id") String id,Pageable pageable){
        MarvelComics comic = comicsService.getComicsById(id);
        if(comic ==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Page<MarvelCharacters> page = comicsService.getCharactersByComic(comic,pageable);
        if(page == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(page,HttpStatus.OK);
    }

    @ApiOperation("создать новый комикс")
    @PostMapping("/comics")
    public ResponseEntity<MarvelComics> createNewComic(@RequestParam String title, @RequestParam String writer,
                                                       @RequestParam String description,
                                                       @RequestParam(required = false,name = "dateOfPublication") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfPublication,
                                                       @RequestParam(required = false, name ="photo") MultipartFile photo,
                                                       @RequestParam List<String> characters){
        MarvelComics createdComic = comicsService.createComic(title,writer,description,dateOfPublication,photo,characters);
        if(createdComic==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdComic,HttpStatus.CREATED);
    }

    @ApiOperation("изменить текущий комикс")
    @PutMapping("/comics/{id}")
    public ResponseEntity<MarvelComics> updateComic(@PathVariable("id") String id,
                                                    @RequestParam String title, @RequestParam String writer,
                                                    @RequestParam String description,
                                                    @RequestParam(required = false,name = "dateOfPublication") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfPublication,
                                                    @RequestParam(required = false, name ="photo") MultipartFile photo,
                                                    @RequestParam List<String> characters) {
        MarvelComics editComic =comicsService.getComicsById(id);
        if (editComic==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        MarvelComics comic = comicsService.editComic(editComic,title,writer,description,dateOfPublication,photo,characters);
        if(comic==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
        return new ResponseEntity<>(comic,HttpStatus.OK);
    }

    @ApiOperation("удалить текущий комикс")
    @DeleteMapping("/comics/{id}")
    public ResponseEntity<MarvelComics> deleteComic(@PathVariable("id") String id){
        MarvelComics comic =comicsService.getComicsById(id);
        if (comic==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        comicsService.deleteComic(comic);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
