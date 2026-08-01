package com.gprosupport.backend.client;

import com.gprosupport.backend.client.dto.ClientRequest;
import com.gprosupport.backend.client.dto.ClientResponse;
import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.projet.ProjetErp;
import com.gprosupport.backend.projet.ProjetErpRepository;
import com.gprosupport.backend.version.VersionErp;
import com.gprosupport.backend.version.VersionErpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ProjetErpRepository projetRepository;
    private final VersionErpRepository versionRepository;

    @Transactional(readOnly = true)
    public List<ClientResponse> findAll(Long projetId) {
        List<Client> clients = (projetId != null)
                ? clientRepository.findByProjetErpId(projetId)
                : clientRepository.findAllWithRelations();
        return clients.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public ClientResponse create(ClientRequest request) {
        ProjetErp projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet", request.getProjetId()));

        VersionErp version = versionRepository.findById(request.getVersionActiveId())
                .orElseThrow(() -> new ResourceNotFoundException("Version", request.getVersionActiveId()));

        // Vérifie que la version appartient bien au projet
        if (!version.getProjetErp().getId().equals(request.getProjetId())) {
            throw new com.gprosupport.backend.common.exception.BusinessException(
                "Cette version n'appartient pas au projet sélectionné."
            );
        }

        Client client = Client.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .projetErp(projet)
                .versionActive(version)
                .build();

        return toResponse(clientRepository.save(client));
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = getOrThrow(id);

        VersionErp version = versionRepository.findById(request.getVersionActiveId())
                .orElseThrow(() -> new ResourceNotFoundException("Version", request.getVersionActiveId()));

        client.setNom(request.getNom());
        client.setEmail(request.getEmail());
        client.setVersionActive(version);

        return toResponse(client);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private Client getOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    private ClientResponse toResponse(Client c) {
        return ClientResponse.builder()
                .id(c.getId())
                .nom(c.getNom())
                .email(c.getEmail())
                .projetId(c.getProjetErp().getId())
                .projetNom(c.getProjetErp().getNom())
                .versionActiveId(c.getVersionActive().getId())
                .versionActiveCode(c.getVersionActive().getCodeVersion())
                .build();
    }
}
