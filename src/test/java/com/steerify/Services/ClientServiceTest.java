package com.steerify.Services;

import com.steerify.Dtos.ClientDto;
import com.steerify.Entities.Client;
import com.steerify.Enums.Role;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Services.impl.ClientServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@DataMongoTest
@Import({ClientServiceImpl.class, BCryptPasswordEncoder.class})
class ClientServiceTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthService authService;

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(Client.class);
    }

    @Test
    void createClient_ShouldSaveClientWithEncryptedPassword() {

        ClientDto clientDto = new ClientDto();
        clientDto.setCompanyName("Test Company");
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setEmail("john.doe@example.com");
        clientDto.setPhone("1234567890");
        clientDto.setPassword("password123");
        clientDto.setRole(Role.CLIENT);
        clientDto.setDescription("Test description");
        clientDto.setContactPerson("John Doe");

        ClientDto result = clientService.createClient(clientDto);

        assertNotNull(result.getClientId());
        assertEquals("Test Company", result.getCompanyName());

        Client savedClient = clientRepository.findById(result.getClientId()).orElseThrow();
        assertTrue(passwordEncoder.matches("password123", savedClient.getPassword()));
    }

    @Test
    void getClientById_ShouldReturnClient_WhenExists() {
        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        client.setCompanyName("Test Company");
        client.setFirstName("Test");
        client.setLastName("User");
        client.setEmail("test@example.com");
        client.setPhone("1234567890");
        client.setPassword(passwordEncoder.encode("password"));
        client.setRole(Role.CLIENT);
        clientRepository.save(client);

        ClientDto result = clientService.getClientById(client.getClientId());

        assertNotNull(result);
        assertEquals(client.getClientId(), result.getClientId());
        assertEquals(client.getCompanyName(), result.getCompanyName());
    }

    @Test
    void updateClient_ShouldUpdateClientDetails() {
        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        client.setCompanyName("Old Name");
        client.setEmail("test@example.com");
        client.setPhone("1234567890");
        client.setPassword(passwordEncoder.encode("password"));
        client.setRole(Role.CLIENT);
        clientRepository.save(client);

        ClientDto updateDto = new ClientDto();
        updateDto.setCompanyName("New Name");
        updateDto.setPhone("9876543210");

        ClientDto result = clientService.updateClient(client.getClientId(), updateDto);

        assertNotNull(result);
        assertEquals("New Name", result.getCompanyName());
        assertEquals("9876543210", result.getPhone());
    }
}