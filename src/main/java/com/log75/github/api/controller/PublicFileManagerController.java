package com.log75.github.api.controller;

import com.log75.github.api.facade.FileManagerFacade;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/v1/fm/files")
public class PublicFileManagerController {

  private final FileManagerFacade fileManagerFacade;

  @GetMapping(path = "/stream/temp/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  @SneakyThrows
  public void downloadAndDelete(@PathVariable String id, HttpServletResponse response) {
    fileManagerFacade.downloadAndDelete(id, response);
  }
}
