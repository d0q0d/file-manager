package com.log75.github;

import com.log75.github.dal.entity.Folder;
import com.log75.github.dal.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataInitializer {

    private final FolderRepository folderRepository;
    @Value( "${folder.name.default:root}")
    private String defaultFolderName;

    public DataInitializer(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @PostConstruct
    public void initializeRequirements(){
        new Thread(this::initializeRootFolder).start();
    }

    private void initializeRootFolder(){
        List<Folder> folderList = folderRepository.findAllByFolderName(defaultFolderName);
        if (folderList.isEmpty()){
            Folder folder = new Folder();
            folder.setFolderName(defaultFolderName);
            folderRepository.save(folder);
        }
    }

}
