package com.steerify.Services;

import com.steerify.Dtos.ClientDto;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.exceptions.ResourceAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    ClientDto createClient(ClientDto clientDto);

    ClientDto getClientById(UUID clientId);

    List<ClientDto> getAllClients();

    ClientDto updateClient(UUID clientId, ClientDto clientDto);

    void deleteClient(UUID clientId);

    ClientDto getClientByEmail(String email);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
