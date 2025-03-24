package com.steerify.Mappers;

import com.steerify.Dtos.ClientDto;
import com.steerify.Entities.Client;

public class ClientMapper {
    public static Client mapToClient(ClientDto clientDto){
        return new Client(
                clientDto.getClientId(),
                clientDto.getCompanyName(),
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getEmail(),
                clientDto.getDescription(),
                clientDto.getContactPerson(),
                clientDto.getPhone(),
                clientDto.getPassword(),
                clientDto.getRole()
        );
    }

    public static ClientDto mapToCLientDto(Client client){
        return new ClientDto(
                client.getClientId(),
                client.getCompanyName(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getDescription(),
                client.getContactPerson(),
                client.getPhone(),
                client.getRole()
        );
    }
}
