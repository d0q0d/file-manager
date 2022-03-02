package com.log75.github.api.controller;

import com.log75.github.api.dto.FileDto;
import com.log75.github.api.dto.UploadFileDto;
import com.log75.github.api.facade.FileManagerFacade;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/fm/files")
@RequiredArgsConstructor
public class FileManagerController {

    private final FileManagerFacade fileManagerFacade;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAuthority('api.v1.fm.files.upload')")
    @ResponseStatus(HttpStatus.CREATED)
    public FileDto uploadFile(@ModelAttribute UploadFileDto uploadFileDto) throws IOException {
        return fileManagerFacade.uploadFile(uploadFileDto);
    }

    @GetMapping
    @PreAuthorize(value = "hasAuthority('api.v1.fm.files.show.page')")
    public Page<FileDto> getFilesPage(@ApiParam(example = "610a3cca1d53024081f94400") String folderId, PageQueryParams pageQueryParams) {
        return fileManagerFacade.getFolderFilesPage(folderId, pageQueryParams);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize(value = "hasAuthority('api.v1.fm.files.show')")
    public FileDto getFile(@PathVariable("id") @ApiParam(example = "611b65b71d53024081f94401") String id) {
        return fileManagerFacade.getFile(id);
    }

    @GetMapping(path = "/stream/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize(value = "hasAuthority('api.v1.fm.files.download')")
    @SneakyThrows
    public void downloadFile(@PathVariable("id") @ApiParam(example = "611b65b71d53024081f94401") String id, HttpServletResponse response) {
        fileManagerFacade.downloadFile(id, response);
    }

}
