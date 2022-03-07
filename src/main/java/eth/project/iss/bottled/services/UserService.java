package eth.project.iss.bottled.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eth.project.iss.bottled.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUserIfNotExist(String walletAddress) {
        System.out.println("Checking for user");
        if (!userRepository.findUser(walletAddress)) {
            System.out.println("User does not exist. Creating.");
            userRepository.createUser(walletAddress);
        }
        System.out.println("User exists. Return.");
    }
}
