package com.steerify.Services.impl;

import com.steerify.Dtos.ClientDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Enums.TalentEnum;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Mappers.ClientMapper;
import com.steerify.Mappers.TalentMapper;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.JwtUserRepository;
import com.steerify.Services.ClientService;
import com.steerify.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtUserRepository jwtUserRepository;

    @Override
    public ClientDto createClient(ClientDto clientDto) {

        if( !clientRepository.existsByEmail(clientDto.getEmail()) ) {
            Client client = ClientMapper.mapToClient(clientDto);
            client.setRole(Role.CLIENT);
            client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            client.setClientId(UUID.randomUUID());
            Client savedClient = clientRepository.save(client);
            jwtUserRepository.save(savedClient);
            return ClientMapper.mapToCLientDto(savedClient);
        }
        throw new ResourceNotFoundException("Client with email: "+clientDto.getEmail()+" already exists");

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        return authService.loginUser(loginRequestDto);
    }

    @Override
    public ClientDto getClientById(UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        return ClientMapper.mapToCLientDto(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::mapToCLientDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ClientDto updateClient(UUID clientId, ClientDto clientDto) {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        existingClient.setCompanyName(clientDto.getCompanyName());
        existingClient.setFirstName(clientDto.getFirstName());
        existingClient.setLastName(clientDto.getLastName());
        existingClient.setDescription(clientDto.getDescription());
        existingClient.setContactPerson(clientDto.getContactPerson());
        existingClient.setPhone(clientDto.getPhone());

        if (clientDto.getPassword() != null && !clientDto.getPassword().isEmpty()) {
            existingClient.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        }

        Client updatedClient = clientRepository.save(existingClient);
        return ClientMapper.mapToCLientDto(updatedClient);
    }

    @Override
    public void deleteClient(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client not found with id: " + clientId);
        }
        clientRepository.deleteById(clientId);
    }

    @Override
    public ClientDto getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with email: " + email));
        return ClientMapper.mapToCLientDto(client);
    }


}
