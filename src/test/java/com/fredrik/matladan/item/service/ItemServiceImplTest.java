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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    //? For security reason we will use mock here
    //? We got a real database setup but it's
    //? Better if we pretend like we have a real database

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImplTest.class);

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
        testingitem.setUnitAmountType(UnitAmountType.LITER);
        //? We set the expiry that to be from now and 7 days from now
        //? This is normaly what milk has
        testingitem.setExpiryDate(LocalDate.now().plusDays(7));

        //? Setup DTO
        createItemDTO = new CreateItemDTO(
                testingitem.getName(),
                testingitem.getStorageLocation(),
                testingitem.getQuantity(),
                testingitem.getSizeOfUnit(),
                testingitem.getUnitAmountType(),
                testingitem.getExpiryDate()
        );

        itemResponseDTO = new ItemResponseDTO(
                1L,
                testingitem.getName(),
                testingitem.getStorageLocation(),
                testingitem.getExpiryDate(),
                LocalDate.now(),  // addedDate
                testingitem.getQuantity(),
                testingitem.getSizeOfUnit(),
                testingitem.getUnitAmountType()
        );


        when(authentication.getName()).thenReturn(testingUser.getUsername());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(customUserRepository.findByUsername("TestUser"))
                .thenReturn(Optional.of(testingUser));

    }

    @Test
    @DisplayName("Should create the item we send in ")
    void createItemTest(){
        //? AAA principal
        //? Arange, ACT, Assert

        logger.info("==== Starting Test ====");
        logger.info("Running test: createItemTest");
        logger.info("Creating an item with name: {}", createItemDTO.name());

        // Arrange
        when(itemMapper.toEntity(createItemDTO, testingUser)).thenReturn(testingitem);
        when(itemRepository.save(testingitem)).thenReturn(testingitem);
        when(itemMapper.toResponseDTO(testingitem)).thenReturn(itemResponseDTO);

        logger.debug("Items has gone through the dto chain and the mapper");

        // Act
        ItemResponseDTO resultFromDTO = itemService.createItem(createItemDTO);


        // Assert
        assertNotNull(resultFromDTO);

        assertEquals(itemResponseDTO.name(), resultFromDTO.name());

        logger.info("Test passed");
        logger.debug("Item was created");
        logger.debug("The response from the mock service matched the responseDTO");
        logger.info("==== Test Ended ====");

    }

    @AfterEach
    void tearDown() {
        logger.info("Cleaning up after the test");
        SecurityContextHolder.clearContext();
    }
}