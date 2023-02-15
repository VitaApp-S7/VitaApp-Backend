package com.vitaquest.userservice.Mock;

import com.vitaquest.userservice.Repositories.IUserRepository;
import com.vitaquest.userservice.Domain.Models.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final IUserRepository userRepository;
    public DataLoader(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //AddMockData();
    }

    private void AddMockData()
    {

        User u1 = new User();
        u1.setId("1");
        u1.setEmail("roy.teneij@gac.nl");
        u1.setName("Roy Teneij");

        User u2 = new User();
        u2.setId("2");
        u2.setEmail("nick.honings@gac.nl");
        u2.setName("Nick Honings");

        User u3 = new User();
        u3.setId("3");
        u3.setEmail("gino.vandegraaf@gac.nl");
        u3.setName("Gino van de Graaf");


//        userRepository.save(u1);
//        userRepository.save(u2);
//        userRepository.save(u3);
    }
}
