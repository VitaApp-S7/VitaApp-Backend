package com.vitaquest.userservice.Repositories;

import com.vitaquest.userservice.Domain.Models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserListRepository extends PagingAndSortingRepository<User, String> {

}
