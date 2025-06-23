import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import model.*;

public class ReservaTest {

    private ClientePF cliente;
    private SalaStandard sala;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Reserva reserva;

    @BeforeEach
    void setup() {
        cliente = new ClientePF("João Silva", "123456789");
        sala = new SalaStandard(10);
        sala.setCodigo(101);
        inicio = LocalDateTime.now().plusDays(2);
        fim = inicio.plusHours(2);
        reserva = new Reserva(cliente, sala, inicio, fim);
    }

    @Test
    void testGetters() {
        assertEquals(cliente, reserva.getCliente());
        assertEquals(sala, reserva.getSala());
        assertEquals(inicio, reserva.getInicio());
        assertEquals(fim, reserva.getFim());
        assertFalse(reserva.isCancelada());
    }

    @Test
    void testCalcularCustoParaClienteComum() {
        double custo = reserva.calcularCusto();
        assertEquals(100.0, custo);
    }

    @Test
    void testCancelarComMaisDe24Horas() {
        assertTrue(reserva.cancelar());
        assertTrue(reserva.isCancelada());
    }

    @Test
    void testCancelarComMenosDe24Horas() {
        LocalDateTime inicioProximo = LocalDateTime.now().plusHours(23);
        LocalDateTime fimProximo = inicioProximo.plusHours(1);
        reserva = new Reserva(cliente, sala, inicioProximo, fimProximo);

        assertFalse(reserva.cancelar());
        assertFalse(reserva.isCancelada());
    }

    @Test
    void testCalcularReembolsoReservaCancelada() {
        reserva.cancelar();
        double reembolso = reserva.calcularReembolso();
        assertEquals(60.0, reembolso);
    }

    @Test
    void testToStringReservaAtiva() {
        String texto = reserva.toString();
        assertTrue(texto.contains("Reserva #"));
        assertTrue(texto.contains("Cliente: João Silva"));
        assertTrue(texto.contains("Status: Ativa"));
    }

    @Test
    void testToStringReservaCanceladaComReembolso() {
        reserva.cancelar();
        String texto = reserva.toString().trim();

        assertTrue(texto.contains("Status: Cancelada"));
        assertTrue(texto.contains("Reembolso: R$ 60,00"));
    }

}