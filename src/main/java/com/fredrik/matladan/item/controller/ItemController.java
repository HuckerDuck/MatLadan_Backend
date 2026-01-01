package com.fredrik.matladan.item.controller;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;
import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.service.ItemService;
import jakarta.persistence.PrePersist;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
//? Requrired that you are atleast logged in
@PreAuthorize("isAuthenticated()")
public class ItemController {
    private final ItemService itemService;
    // Get all items
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems(){
        List<ItemResponseDTO> items = itemService.getAllItemsFromCurrentUser();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ItemResponseDTO>> getAllItemsPaged(Pageable pageable){
        Page<ItemResponseDTO> items = itemService.getAllItemsFromCurrentUser(pageable);
        return ResponseEntity.ok(items);
    }

    // Get items by location
    @GetMapping("/location")
    public ResponseEntity<List<ItemResponseDTO>> getItemsByLocation(
            @RequestParam StorageLocation storageLocation
            )
    {
        List<ItemResponseDTO> items = itemService.getItemsByLocation(storageLocation);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/location/paged")
    public ResponseEntity<Page<ItemResponseDTO>> getItemsByLocationPaged(
             @RequestParam StorageLocation storageLocation,
             Pageable pageable
    ){
        Page <ItemResponseDTO> items = itemService.getItemsByLocation(storageLocation, pageable);
        return ResponseEntity.ok(items);
    }

    // Get and search the item by name
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDTO>> searchItemsByName(
            // Get the value from the query parameter and
            // convert it to a string
            // then its inserted to the method
            @RequestParam ("query") String itemName

    ){
        return ResponseEntity.ok(itemService.searchItemsByName(itemName));
    }

    @GetMapping("/search/paged")
    public ResponseEntity<Page<ItemResponseDTO>> searchItemsByNamePaged(
            @RequestParam ("query") String itemName,
            Pageable pageable

    ){
        return ResponseEntity.ok(itemService.searchItemsByName(itemName, pageable));
    }

    //?
    //? --- Postmappings ---
    //?

    @PostMapping
    public ResponseEntity <ItemResponseDTO> createItem(
            @Valid @RequestBody CreateItemDTO createItemDTO
    ){
        ItemResponseDTO createdItem = itemService.createItem(createItemDTO);

        return ResponseEntity.status(201)
                        .body(createdItem);
    }

    //
    // --- Patchmappings ---
    //
    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody UpdateItemDTO updateItemDTO
    )
    {
        ItemResponseDTO updated = itemService.updateItemFromCurrentUser(id, updateItemDTO);
        return ResponseEntity.ok(updated);
    }

    //
    // --- Delelemappings ---
    //

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
