package com.fri.code.users.services.beans;

import com.fri.code.users.lib.UserMetadata;
import com.fri.code.users.models.converters.UserMetadataConverter;
import com.fri.code.users.models.entities.UserMetadataEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserMetadataBean {

    @Inject
    private EntityManager em;

    public List<UserMetadata> getAllUsers(){
        TypedQuery<UserMetadataEntity> query = em.createNamedQuery("UserMetadataEntity.getAll", UserMetadataEntity.class);
        return query.getResultList().stream().map(UserMetadataConverter::toDTO).collect(Collectors.toList());
    }

    public UserMetadata getUserById(Integer userID){
        UserMetadataEntity user = em.find(UserMetadataEntity.class, userID);
        if (user == null)
            throw new NotFoundException();

        return UserMetadataConverter.toDTO(user);
    }

    public UserMetadata createUserMetadata(UserMetadata userMetadata){
        UserMetadataEntity entity = UserMetadataConverter.toEntity(userMetadata);

        try {
            beginTx();
            em.persist(entity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (entity.getID() == null) {
            throw new RuntimeException("The user was not created");
        }

        return UserMetadataConverter.toDTO(entity);

    }

    public UserMetadata putUserMetadata(Integer userID, UserMetadata userMetadata){
        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);

        if (entity == null) {
            return null;
        }

        UserMetadataEntity updatedEntity = UserMetadataConverter.toEntity(userMetadata);

        try {
            beginTx();
            updatedEntity.setID(entity.getID());
            updatedEntity = em.merge(updatedEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return UserMetadataConverter.toDTO(updatedEntity);

    }

    public boolean deleteUserMetadata(Integer userID){

        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);
        if (entity != null) {
            try {
                beginTx();
                em.remove(entity);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        }
        else return false;
        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }

}
