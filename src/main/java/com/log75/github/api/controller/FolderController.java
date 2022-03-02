package com.log75.github.api.controller;

import com.log75.github.api.dto.CreateFolderDto;
import com.log75.github.api.dto.FolderDto;
import com.log75.github.api.facade.FileManagerFacade;
import com.vasl.restful_query_builder.model.QueryString;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fm/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FileManagerFacade fileManagerFacade;

    @PostMapping
    @PreAuthorize(value = "hasAuthority('api.v1.fm.folders.create')")
    @ResponseStatus(HttpStatus.CREATED)
    public FolderDto createFolder(CreateFolderDto createFolderDto) {
        return fileManagerFacade.createFolder(createFolderDto);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize(value = "hasAuthority('api.v1.fm.folders.getById')")
    public FolderDto getFolder(@PathVariable String id) {
        return fileManagerFacade.getFolder(id);
    }

    public List<FolderDto> getFolderListByQuery(QueryString queryString) {
        return fileManagerFacade.getFolderListByQuery(queryString);
    }
}
