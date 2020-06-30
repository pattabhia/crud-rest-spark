package com.pattabhi.repository;

import com.pattabhi.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, String> {

}
