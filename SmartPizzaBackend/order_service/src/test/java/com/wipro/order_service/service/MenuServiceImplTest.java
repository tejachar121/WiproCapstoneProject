package com.wipro.order_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.wipro.order_service.dto.MenuDTO;
import com.wipro.order_service.entity.MenuEntity;
import com.wipro.order_service.repository.MenuRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MenuServiceImplTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private MenuEntity menu;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // sample entity setup
        menu = new MenuEntity();
        menu.setId(1L);
        menu.setName("Veg Pizza");
        menu.setDescription("Tasty veg pizza");
        menu.setPrice(150.0);
        menu.setCategory("Medium");
        menu.setImageUrl("pizza.jpg");
    }

    @Test
    void testAddMenu() {

        MenuDTO dto = new MenuDTO();
        dto.setName("Veg Pizza");
        dto.setDescription("Tasty veg pizza");
        dto.setPrice(150.0);
        dto.setCategory("Medium");
        dto.setImageUrl("pizza.jpg");

        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menu);

        MenuDTO result = menuService.addMenu(dto);

        assertNotNull(result);
        assertEquals("Veg Pizza", result.getName());
        assertEquals(150.0, result.getPrice());

        verify(menuRepository).save(any(MenuEntity.class));
    }

    @Test
    void testGetAllMenu() {

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));

        List<MenuDTO> list = menuService.getAllMenu();

        assertEquals(1, list.size());
        assertEquals("Veg Pizza", list.get(0).getName());

        verify(menuRepository).findAll();
    }

    @Test
    void testGetMenuById() {

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        MenuDTO dto = menuService.getMenuById(1L);

        assertNotNull(dto);
        assertEquals("Veg Pizza", dto.getName());

        verify(menuRepository).findById(1L);
    }

    @Test
    void testGetMenuById_NotFound() {

        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> menuService.getMenuById(1L));

        assertEquals("Menu not found", ex.getMessage());

        verify(menuRepository).findById(1L);
    }

    @Test
    void testUpdateMenu() {

        MenuDTO dto = new MenuDTO();
        dto.setName("Updated Pizza");
        dto.setDescription("Updated desc");
        dto.setPrice(200.0);
        dto.setCategory("Large");
        dto.setImageUrl("updated.jpg");

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menu);

        MenuDTO result = menuService.updateMenu(1L, dto);

        assertEquals("Updated Pizza", result.getName());
        assertEquals(200.0, result.getPrice());

        verify(menuRepository).findById(1L);
        verify(menuRepository).save(menu);
    }

    @Test
    void testDeleteMenu() {

        doNothing().when(menuRepository).deleteById(1L);

        menuService.deleteMenu(1L);

        verify(menuRepository).deleteById(1L);
    }
}