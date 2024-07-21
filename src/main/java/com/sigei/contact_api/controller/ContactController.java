package com.sigei.contact_api.controller;

import com.sigei.contact_api.dblayer.entity.Contact;
import com.sigei.contact_api.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.sigei.contact_api.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
//        return ResponseEntity.ok(contactService.createContact(contact));
        return ResponseEntity.created(URI.create("api/v1/contacts/userID")).body(contactService.createContact(contact));
    }
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page",defaultValue = "0") int page,
                                                     @RequestParam(value = "size",defaultValue = "10") int size) {
      return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContactById(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id,
                                              @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @GetMapping(path="/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY+ filename));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable("id") String id) {
        try {
            contactService.deleteContactById(id);
            String successMessage = "Contact deleted successfully: " + id;
            return ResponseEntity.ok(successMessage);
        } catch (RuntimeException e) {
            String errorMessage = "Contact not found with the specified id: " + id;
            return ResponseEntity.status(404).body(errorMessage);
        }
    }
}
