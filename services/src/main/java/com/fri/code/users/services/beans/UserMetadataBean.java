package com.fri.code.users.services.beans;

import com.fri.code.users.lib.SubjectMetadata;
import com.fri.code.users.lib.UserMetadata;
import com.fri.code.users.models.converters.UserMetadataConverter;
import com.fri.code.users.models.entities.UserMetadataEntity;
import com.fri.code.users.services.config.IntegrationConfiguration;
import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.security.auth.Subject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@ApplicationScoped
public class UserMetadataBean {

    @Inject
    private EntityManager em;

    private Client httpClient;
    private Logger log = Logger.getLogger(UserMetadataBean.class.getName());

    @Inject
    @DiscoverService(value = "code-subjects")
    private Optional<String> subjectsPath;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<UserMetadata> getAllUsers() {
        TypedQuery<UserMetadataEntity> query = em.createNamedQuery("UserMetadataEntity.getAll", UserMetadataEntity.class);
        return query.getResultList().stream().map(UserMetadataConverter::toDTO).collect(Collectors.toList());
    }

    public UserMetadata getUserById(Integer userID) {
        UserMetadataEntity user = em.find(UserMetadataEntity.class, userID);
        if (user == null)
            throw new NotFoundException();

        return UserMetadataConverter.toDTO(user);
    }

    public List<SubjectMetadata> getSubjectsForUser(Integer userID) {
        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);

        if (entity == null)
            return null;

        List<SubjectMetadata> subjects = new ArrayList<SubjectMetadata>();
        List<Integer> subjectsIDs = entity.getSubjects();

        for (Integer id : subjectsIDs) {
            try {
                SubjectMetadata subject = getSubjectById(id);
                subjects.add(subject);
            } catch (Exception e) {
                //throw new NoSuchElementException();
                continue;
            }

        }

        return subjects;

    }

    public SubjectMetadata getSubjectById(Integer subjectID) {
        if (subjectsPath.isPresent()) {
            try {
                SubjectMetadata subject = httpClient
                        .target(String.format("%s/v1/subjects/%d", subjectsPath.get(), subjectID))
                        .request().get(new GenericType<SubjectMetadata>() {
                        });
                return subject;
            } catch (WebApplicationException | ProcessingException e) {
                throw new InternalServerErrorException(e);
            }
        }

        return null;
    }

    public UserMetadata addSubject(Integer userID, Integer subjectID) {
        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);

        if (entity == null) {
            return null;
        }

        UserMetadata userDto = UserMetadataConverter.toDTO(entity);
        userDto.addSubjectId(subjectID);

        UserMetadataEntity updatedEntity = UserMetadataConverter.toEntity(userDto);

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

    public UserMetadata removeSubject(Integer userID, Integer subjectID) {
        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);

        if (entity == null) {
            return null;
        }

        UserMetadata userDto = UserMetadataConverter.toDTO(entity);
        userDto.removeSubjectId(subjectID);

        UserMetadataEntity updatedEntity = UserMetadataConverter.toEntity(userDto);

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

    public UserMetadata createUserMetadata(UserMetadata userMetadata) {
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

    public UserMetadata putUserMetadata(Integer userID, UserMetadata userMetadata) {
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

    public boolean deleteUserMetadata(Integer userID) {

        UserMetadataEntity entity = em.find(UserMetadataEntity.class, userID);
        if (entity != null) {
            try {
                beginTx();
                em.remove(entity);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else return false;
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
