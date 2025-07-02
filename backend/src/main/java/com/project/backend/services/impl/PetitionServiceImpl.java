package com.project.backend.services.impl;

import com.project.backend.models.petitions.Petition;
import com.project.backend.services.inter.PetitionService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public class PetitionServiceImpl implements PetitionService {
    @Override
    public Petition create(Petition petitionRequest) {
        return null;
    }

    @Override
    public Petition update(Petition petitionRequest) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void support(long petitionId, long userId) {

    }

    @Override
    public void accept(long petitionId) {

    }

    @Override
    public void reject(long petitionId) {

    }

    @Override
    public Petition findById(long id) {
        return null;
    }

    @Override
    public Page<Petition> findAllMy(String name, String status, long page, long size, Authentication auth) {
        return null;
    }

    @Override
    public Page<Petition> findAllByCreator(String name, String status, long page, long size, Authentication auth) {
        return null;
    }

    @Override
    public Page<Petition> findAllForDirector(String name, String status, long page, long size, Authentication auth) {
        return null;
    }
}
