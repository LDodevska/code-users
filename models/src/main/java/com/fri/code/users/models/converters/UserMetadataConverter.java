package com.fri.code.users.models.converters;

import com.fri.code.users.lib.UserMetadata;
import com.fri.code.users.models.entities.UserMetadataEntity;

public class UserMetadataConverter {

    public static UserMetadata toDTO(UserMetadataEntity userMetadataEntity){
        UserMetadata userMetadata = new UserMetadata();
        userMetadata.setID(userMetadataEntity.getID());
        userMetadata.setFirstName(userMetadataEntity.getFirstName());
        userMetadata.setLastName(userMetadataEntity.getLastName());
        userMetadata.setEmail(userMetadataEntity.getEmail());
        userMetadata.setSubjects(userMetadataEntity.getSubjects());
        userMetadata.setUsername(userMetadataEntity.getUsername());
        userMetadata.setPassword(userMetadataEntity.getPassword());
        userMetadata.setRole(userMetadataEntity.getRole());
        userMetadata.setAccessToken(userMetadataEntity.getAccessToken());

        return userMetadata;
    }

    public static UserMetadataEntity toEntity(UserMetadata userMetadata){
        UserMetadataEntity entity = new UserMetadataEntity();
        entity.setID(userMetadata.getID());
        entity.setFirstName(userMetadata.getFirstName());
        entity.setLastName(userMetadata.getLastName());
        entity.setEmail(userMetadata.getEmail());
        entity.setSubjects(userMetadata.getSubjects());
        entity.setUsername(userMetadata.getUsername());
        entity.setPassword(userMetadata.getPassword());
        entity.setRole(userMetadata.getRole());
        entity.setAccessToken(userMetadata.getAccessToken());

        return entity;
    }
}
