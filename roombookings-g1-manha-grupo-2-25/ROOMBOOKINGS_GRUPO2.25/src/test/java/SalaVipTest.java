import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.SalaRecurso;
import model.Sala;

import static org.junit.jupiter.api.Assertions.*;

public class SalaVipTest {

    private SalaVip salaVip;

    @BeforeEach
    void setUp() {
        salaVip = new SalaVip(20);
        salaVip.setCodigo(301);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(301, salaVip.getCodigo());
        assertEquals(20, salaVip.getCapacidade());
        assertNotNull(salaVip.getRecursos());
        assertEquals(4, salaVip.getRecursos().size());
    }

    @Test
    void testAdicionarRecurso() {
        SalaRecurso salaRecursoExtra = new SalaRecurso("Sistema de Som", "Sistema de som de alta qualidade");
        salaVip.adicionarRecurso(salaRecursoExtra);

        assertEquals(5, salaVip.getRecursos().size());
        assertTrue(salaVip.getRecursos().contains(salaRecursoExtra));
    }

    @Test
    void testToStringComRecursos() {
        String resultado = salaVip.toString();
        assertTrue(resultado.contains("Sala VIP"));
        assertTrue(resultado.contains("Projetor: Projetor de alta definição"));
        assertTrue(resultado.contains("Ar-condicionado: Ar-condicionado moderno"));
        assertTrue(resultado.contains("Videoconferência: Sistema de videoconferência"));
        assertTrue(resultado.contains("Quadro Branco: Quadro branco para anotações"));
    }

    @Test
    void testCalcularCusto() {
        double custo = salaVip.calcularCusto(3);
        assertEquals(3 * Sala.VALOR_BASE_HORA * 1.30, custo, 0.01);
    }

    @Test
    void testCalcularReembolso() {
        double valor = 300.0;
        double reembolso = salaVip.calcularReembolso(valor);
        assertEquals(valor * 0.3, reembolso, 0.01);
    }
}