package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.Entity.Unit;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitControllerTest {

	@Autowired
	@InjectMocks
	private UnitController unitController;
	@Mock
	private UnitService unitService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void getUnitShouldCallAndReturnTest() throws ApplicationErrorException {
		List<Unit> unitList = new ArrayList<>();
		when(unitService.list()).thenReturn(unitList);
		assertNotNull(unitController.getAll());
		verify(unitService, times(1)).list();
	}

	@Test
	void getUnitShouldCatchAndRethrowException() throws ApplicationErrorException {
		when(unitService.list()).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> unitController.getAll());
		verifyNoMoreInteractions(unitService);
	}

	@Test
	void addUnitShouldCallAndReturnTest() throws Exception {
		Unit unit = new Unit("kilogram", "kg", "Unit of Larger Masses", true);
		when(unitService.create(unit)).thenReturn(unit);
		assertEquals(unit, unitController.add(unit));
		verify(unitService, times(1)).create(unit);
	}

	@Test
	void addUnitShouldCatchAndRethrowExceptionAndNoMoreInteractions() throws Exception {
		Unit unit = new Unit("kilogram", "kg", "Unit of Larger Masses", true);
		when(unitService.create(unit)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> unitController.add(unit));
		verifyNoMoreInteractions(unitService);
	}

	@Test
	void editUnitShouldCallAndReturn() throws Exception {
		Unit unit = new Unit("kilogram", "kg", "Unit of Larger Masses", true);
		when(unitService.edit(unit)).thenReturn(unit);
		assertEquals(unit, unitController.edit(unit));
		verify(unitService, times(1)).edit(unit);
	}

	@Test
	void editUnitShouldCatchAndRethrowException() throws Exception {
		Unit unit = new Unit("kilogram", "kg", "Unit of Larger Masses", true);
		when(unitService.edit(unit)).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> unitController.edit(unit));
		verifyNoMoreInteractions(unitService);
	}

	@Test
	void editUnitShouldThrowExceptionWhenNullReturned() throws Exception {
		Unit unit = new Unit("kilogram", "kg", "Unit of Larger Masses", true);
		when(unitService.edit(unit)).thenReturn(null);
		assertThrows(InvalidTemplateException.class, () -> unitController.edit(unit));
		verifyNoMoreInteractions(unitService);
	}

	@Test
	void deleteUnitShouldCallAndReturn() throws Exception {
		String parameter = "G01";
		when(unitService.delete(parameter)).thenReturn(1);
		assertEquals(1, unitController.delete(parameter));
		verify(unitService, times(1)).delete(parameter);
	}

	@Test
	void deleteUnitShouldCatchANdRethrowException() throws Exception {
		String parameter = "G01";
		when(unitService.delete(parameter)).thenThrow(ApplicationErrorException.class);
		assertThrows(ApplicationErrorException.class, () -> unitController.delete(parameter));
		verifyNoMoreInteractions(unitService);
	}

	@Test
	void deleteUnitShouldThrowExceptionWhenStatusCodeZero() throws Exception {
		String parameter = "G01";
		when(unitService.delete(parameter)).thenReturn(0);
		assertThrows(InvalidTemplateException.class, () -> unitController.delete(parameter));
		verifyNoMoreInteractions(unitService);
	}
}
