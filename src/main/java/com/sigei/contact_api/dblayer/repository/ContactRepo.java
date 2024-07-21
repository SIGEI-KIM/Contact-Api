package com.sigei.contact_api.dblayer.repository;

import com.sigei.contact_api.dblayer.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepo extends JpaRepository<Contact, String> {
    Optional<Contact> findByEmail(String id);
}
