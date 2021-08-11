package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	Cuenta cuenta = new Cuenta("Sergio", new BigDecimal("1000.12345"));
	TestInfo testInfo;
	TestReporter testReporter;
	
	@Test
	@DisplayName("Dado que la persona es Junior, el nombre esperado es Junior")
	void testNombreCuenta() {		
		String esperado = "Junior";
		String real = cuenta.getPersona();
		assertEquals(esperado, real);
		assertTrue(real.equals(esperado));
	}

	@Test
	@DisplayName("El saldo, que no sea null, mayor que cero, valor esperado")
	void testSaldoCuenta(){
		assertNotNull(cuenta.getSaldo());
		assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
		assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
	}

	@Test
	@DisplayName("testeando referencias que sean iguales con el método equals")
	void testReferenciaCuenta(){
		cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		//assertNotEquals(cuenta2, cuenta);
		assertEquals(cuenta2, cuenta);
	}

	@Test
	@DisplayName("Testeando saldos de la cuenta débito")
	void debitoTest() throws DineroInsuficienteException{
		cuenta.debito(new BigDecimal("500.100"));
		assertNotNull(cuenta.getSaldo());
		int esperado = 500;
		assertEquals(esperado, cuenta.getSaldo().intValue());
		String esperado2 = "500.02345";
		assertEquals(esperado2, cuenta.getSaldo().toString());
	}


	@Test
	@DisplayName("Testeando saldo de la cuenta crédito")
	void creditoTest(){
		cuenta.credito(new BigDecimal("500000.2568"));
		int esperado = 501000;
		assertNotNull(cuenta.getSaldo());
		assertEquals(esperado, cuenta.getSaldo().intValue());
		String esperado2 = "501000.38025";
		assertEquals(esperado2, cuenta.getSaldo().toString());
	}

	@Test
	@DisplayName("Dado que tengo un saldo, quiero generar una excepción con saldo insuficiente")
	@Tag("Error")
	void dineroInsuficienteTest(){
		System.out.println(cuenta.getSaldo());
		Exception exception = assertThrows(DineroInsuficienteException.class, ()-> {
			cuenta.debito(new BigDecimal(1500));
		});
		String actual = exception.getMessage();
		String esperado = "No tiene dinero en la cuenta";
		assertEquals(esperado, actual);
	}

	@Test
	@DisplayName("probando assertAll")
	void testRelacionBancoCuentas(){
		Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
		Cuenta cuenta2 = new Cuenta("Sergio", new BigDecimal("1500.8989"));

		Banco banco = new Banco();
		banco.addCuenta(cuenta1);
		banco.addCuenta(cuenta2);

		banco.setNombre("Banco del Estado");
		banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
		cuenta1.setBanco(banco);

		assertAll(
			()->assertEquals("1000.8989", cuenta2.getSaldo().toString(),
				()->"el valor del saldo de la cuenta2 no es el esperado"),
			()->assertEquals("3000", cuenta1.getSaldo().toString(),
				()->"El valor del saldo de la cuenta1 no es el esperado"),
			()->assertEquals(2, banco.getCuentas().size(),
				()->"El banco no tienes las cuentas esperadas"),
			()->assertEquals("Banco del Estado", cuenta1.getBanco().getNombre(), ()->"El nombre del banco no coincide"),
			()->assertEquals("Junior", banco.getCuentas().stream()
					.filter(c->c.getPersona().equals("Junior"))
					.findFirst()
					.get().getPersona(), ()-> "No existe ningúna coincidencia en las cuents con el nombre Junior"),
			()->assertTrue(banco.getCuentas().stream().anyMatch(c->c.getPersona().equals("John Doe")),
					()->"No existe ninguna coincidencia con el nombre John Doe"));
	}

	/**
	 * Se ejecutará antes de cada método
	 */
	@BeforeEach
	void initMetodoTest(TestInfo testInfo,TestReporter testReporter){
		this.cuenta = new Cuenta("Junior", new BigDecimal("1000.12345"));
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		System.out.println("Iniciando el método");
		testReporter.publishEntry("Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName() 
			+"Con las eiquetas" + testInfo.getTags());
	}

	/**
	 * Se ejecutará al final de cada método
	 */
	@AfterEach
	void tearDown(){
		System.out.println("Finalizando el método de prueba");
	}

	/**
	 * se ejecutará antes de cualquier otro método
	 */
	@BeforeAll
	static void BeforeAll(){
		System.out.println("Iniciando el test");
	}

	/**
	 * Se ejecutará cuando no hayan más métodos para ejecutar
	 */
	@AfterAll
	static void AfterAll(){
		System.out.println("Finalizando el test");
	}

	@Test
	@DisplayName("Test saldo cuenta dev")
	void testSaldoCuentaDev(){
		boolean esDev = "dev".equals(System.getProperty("ENV"));
		assumeTrue(esDev);
		assertNotNull(cuenta.getSaldo());
		assertEquals(1000.123456, cuenta.getSaldo().doubleValue());
		assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
	}

	/**
	 * Test Parametrizados
	 * @throws DineroInsuficienteException
	*/
	@ParameterizedTest
	@ValueSource(strings = {"100","200","300","500","700","900.12345"})
	void testDebitoValueSource(String monto) throws DineroInsuficienteException{
		cuenta.debito(new BigDecimal(monto));
		assertNotNull(cuenta.getSaldo());
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
	}

	@ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
	@CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,900.12345"})
	void testDebitoCuentaCsvSource(String index, String monto) throws DineroInsuficienteException{
		System.out.println(index + "->" +monto);
		cuenta.debito(new BigDecimal(monto));
		assertNotNull(cuenta.getSaldo());
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
	}

	
}
