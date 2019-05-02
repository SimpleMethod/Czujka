package pl.simplemethod.czujka.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsersRepositoryImpl {

    @Autowired
    private UsersRepository repository;
}
