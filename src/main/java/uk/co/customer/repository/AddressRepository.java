package uk.co.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.co.customer.domain.Address;
import uk.co.customer.domain.Customer;

@RepositoryRestResource(collectionResourceRel = "address", path = "address")
public interface AddressRepository extends MongoRepository<Address, String> {
}
