package com.fredrik.matladan.item.controller;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class itemController {
    private final ItemService itemService;

    public itemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems(){
        List<ItemResponseDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity <ItemResponseDTO> createItem(
            @Valid @RequestBody CreateItemDTO createItemDTO
    ){
        ItemResponseDTO createdItem = itemService.createItem(createItemDTO);

        return ResponseEntity.status(201).body(createdItem);
    }
}
