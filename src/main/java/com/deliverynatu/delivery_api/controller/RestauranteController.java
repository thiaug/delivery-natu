package com.deliverynatu.delivery_api.controller;

import com.deliverynatu.delivery_api.dto.request.RestauranteRequest;
import com.deliverynatu.delivery_api.dto.response.RestauranteResponse;
import com.deliverynatu.delivery_api.entity.Restaurante;
import com.deliverynatu.delivery_api.service.RestauranteService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;
    private final ModelMapper modelMapper;

    @Autowired
    public RestauranteController(RestauranteService restauranteService, ModelMapper modelMapper) {
        this.restauranteService = restauranteService;
        this.modelMapper = modelMapper;
    }

    private RestauranteResponse convertToResponse(Restaurante restaurante) {
        return modelMapper.map(restaurante, RestauranteResponse.class);
    }

    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(@Valid @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante restaurante = modelMapper.map(restauranteRequest, Restaurante.class);
        Restaurante novoRestaurante = restauranteService.cadastrar(restaurante);
        return new ResponseEntity<>(convertToResponse(novoRestaurante), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
                .map(restaurante -> ResponseEntity.ok(convertToResponse(restaurante)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listarAtivos() {
        List<RestauranteResponse> responses = restauranteService.listarAtivos().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponse>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponse> responses = restauranteService.buscarPorCategoria(categoria).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante restaurante = modelMapper.map(restauranteRequest, Restaurante.class);
        Restaurante restauranteAtualizado = restauranteService.atualizar(id, restaurante);
        return ResponseEntity.ok(convertToResponse(restauranteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}