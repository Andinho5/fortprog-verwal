package org.example.mail;

import org.example.Main;
import org.example.service.ModelService;

import java.util.List;
import java.util.Optional;

public class BBMailService implements ModelService<BBMail> {
    @Override
    public Optional<BBMail> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<BBMail> findAll() {
        return List.of();
    }

    @Override
    public void save(BBMail bbMail) {

    }
}
