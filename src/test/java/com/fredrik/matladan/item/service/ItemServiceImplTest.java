package com.fredrik.matladan.item.service;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.enums.UnitAmountType;
import com.fredrik.matladan.item.mapper.ItemMapper;
import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    //? For security reason we will use mock here
    //? We got a real database setup but it's
    //? Better if we pretend like we have a real database

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CustomUserRepository customUserRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    //? We inject mocks into the service that we wanna use in the test
    @InjectMocks
    private ItemServiceImpl itemService;

    private CustomUser testingUser;
    private Item testingitem;
    private CreateItemDTO createItemDTO;
    private ItemResponseDTO itemResponseDTO;

    @BeforeEach
    void setUp() {
        //? Add a Test User that we will use
        testingUser = new CustomUser();
        testingUser.setUsername("TestUser");
        testingUser.setEmail("");

        //? Add a Testing item that we will use
        testingitem = new Item();
        testingitem.setName("Milk");
        testingitem.setStorageLocation(StorageLocation.FRIDGE);
        testingitem.setQuantity(2);
        testingitem.setSizeOfUnit(1.0);
        testingitem.setUnitAmount(UnitAmountType.LITER);
        //? We set the expiry that to be from now and 7 days from now
        //? This is normaly what milk has
        testingitem.setExpiryDate(LocalDate.now().plusDays(7));

        //? Setup DTO
        createItemDTO = new CreateItemDTO(
                testingitem.getName(),
                testingitem.getStorageLocation(),
                testingitem.getQuantity(),
                testingitem.getSizeOfUnit(),
                testingitem.getUnitAmount(),
                testingitem.getExpiryDate()
        );

    }

    @Test
    @DisplayName("Should create the item we send in ")
    void createItemTest(){
        //? AAA principal
        //? Arange, ACT, Assert

        // Arrange
        when(itemMapper.toEntity(createItemDTO, testingUser)).thenReturn(testingitem);
        when(itemRepository.save(testingitem)).thenReturn(testingitem);
        when(itemMapper.toResponseDTO(testingitem)).thenReturn(itemResponseDTO);

        // Act
        ItemResponseDTO resultFromDTO = itemService.createItem(createItemDTO);

        // Assert
        assert(resultFromDTO.equals(itemResponseDTO));
    }

    @AfterEach
    void tearDown() {
    }
}