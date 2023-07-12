package org.example.Service;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.UnitDAO;
import org.example.Entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplementationTest {

	@Autowired
	@InjectMocks
	private UnitServiceImplementation unitService;
	@Mock
	private UnitDAO unitDAO;

	public static Stream<Arguments> unitProvider() {
		return Stream.of(Arguments.of(new Unit("Kilogr32am", "kg", "Unit of Larger Masses", true)),
				Arguments.of(new Unit("Kilogram", "kgndalndsa", "Unit of Larger Masses", true)));
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createShouldCallAndReturnUnit() throws Exception {
		Unit unit = new Unit("Kilogram", "kg", "Unit of Larger Masses", true);
		when(unitDAO.create(unit)).thenReturn(unit);
		Unit createdUnit = unitService.create(unit);
		assertNotNull(createdUnit);
		assertEquals(unit, createdUnit);
		verify(unitDAO, times(1)).create(unit);
	}

	@ParameterizedTest
	@MethodSource("unitProvider")
	void createShouldThrowExceptionWhenFormatMismatch(Unit unit) {
		assertThrows(InvalidTemplateException.class, () -> unitService.create(unit));
		verifyNoInteractions(unitDAO);
	}

	@Test
	void createUnitNullValidation() {
		assertThrows(NullPointerException.class, () -> unitService.create(null));
		verifyNoInteractions(unitDAO);
	}

	@Test
	void listCallsAndReturnsList() throws ApplicationErrorException {
		when(unitDAO.list()).thenReturn(Arrays.asList(new Unit(), new Unit()));
		List<Unit> unitList = unitService.list();
		assertNotNull(unitList);
		verify(unitDAO, times(1)).list();
		verifyNoMoreInteractions(unitDAO);
	}

	@Test
	void editShouldCallAndReturnUnit() throws Exception {
		Unit unit = new Unit("Kilogram", "kg", "Unit of Larger Solid Masses", true);
		when(unitDAO.edit(unit)).thenReturn(unit);
		Unit editedUnit = unitService.edit(unit);
		assertNotNull(editedUnit);
		assertEquals(unit, editedUnit);
		verify(unitDAO, times(1)).edit(unit);
	}

	@ParameterizedTest
	@MethodSource("unitProvider")
	void editShouldThrowExceptionWhenFormatMismatch(Unit unit) {
		assertThrows(InvalidTemplateException.class, () -> unitService.edit(unit));
		verifyNoInteractions(unitDAO);
	}

	@Test
	void editProductNullValidation() {
		assertThrows(NullPointerException.class, () -> unitService.edit(null));
		verifyNoInteractions(unitDAO);
	}

	@Test
	void deleteShouldCallAndReturnStatusCode() throws Exception {
		String code = "kg";
		when(unitDAO.delete(anyString())).thenReturn(1);
		assertEquals(1, unitService.delete(code));
		verify(unitDAO, times(1)).delete(code);
	}

	@Test
	void deleteUserNullValidation() throws Exception {
		assertEquals(- 1, unitService.delete(null));
		verifyNoInteractions(unitDAO);
	}
}