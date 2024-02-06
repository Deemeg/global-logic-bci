package com.globallogic.bcigloballogic.repository;

import com.globallogic.bcigloballogic.model.entity.PhoneEntity;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhoneRepository extends CrudRepository<PhoneEntity, UUID> {


}
