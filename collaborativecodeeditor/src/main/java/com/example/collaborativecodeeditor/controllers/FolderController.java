package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.FileEntity;
import com.example.collaborativecodeeditor.model.Folder;
import com.example.collaborativecodeeditor.repository.FileRepository;
import com.example.collaborativecodeeditor.repository.FolderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderRepository folderRepo;
    private final FileRepository fileRepo;

    public FolderController(FolderRepository folderRepo, FileRepository fileRepo) {
        this.folderRepo = folderRepo;
        this.fileRepo = fileRepo;
    }

    @GetMapping
    public List<Folder> getAllFolders() {
        return folderRepo.findAll();
    }

    @PostMapping
    public Folder createFolder(@RequestBody Map<String, String> body) {
        Folder f = new Folder();
        f.setName(body.get("name"));
        return folderRepo.save(f);
    }

    @PostMapping("/{folderId}/files")
    public FileEntity createFile(@PathVariable Long folderId, @RequestBody Map<String, String> body) {
        Folder folder = folderRepo.findById(folderId).orElseThrow();
        FileEntity file = new FileEntity();
        file.setName(body.get("name"));
        file.setContent(body.getOrDefault("content", ""));
        file.setFolder(folder);
        return fileRepo.save(file);
    }

    @PutMapping("/files/{fileId}")
    public FileEntity updateFile(@PathVariable Long fileId, @RequestBody Map<String, String> body) {
        FileEntity file = fileRepo.findById(fileId).orElseThrow();
        file.setContent(body.get("content"));
        return fileRepo.save(file);
    }
}
