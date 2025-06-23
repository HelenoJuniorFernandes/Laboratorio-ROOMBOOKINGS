import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.SalaRecurso;
import model.Sala;

import static org.junit.jupiter.api.Assertions.*;

public class SalaPremiumTest {

    private SalaPremium salaPremium;

    @BeforeEach
    void setUp() {
        salaPremium = new SalaPremium(15);
        salaPremium.setCodigo(201);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(201, salaPremium.getCodigo());
        assertEquals(15, salaPremium.getCapacidade());
        assertNotNull(salaPremium.getRecursos());
        assertEquals(2, salaPremium.getRecursos().size());
    }

    @Test
    void testAdicionarRecurso() {
        SalaRecurso salaRecursoExtra = new SalaRecurso("Sound System", "Sistema de som de alta qualidade");
        salaPremium.adicionarRecurso(salaRecursoExtra);

        assertEquals(3, salaPremium.getRecursos().size());
        assertTrue(salaPremium.getRecursos().contains(salaRecursoExtra));
    }

    @Test
    void testToStringComRecursos() {
        String resultado = salaPremium.toString();
        assertTrue(resultado.contains("Sala Premium"));
        assertTrue(resultado.contains("Projetor: Projetor de alta definição"));
        assertTrue(resultado.contains("Ar-condicionado: Ar-condicionado moderno"));
    }

    @Test
    void testCalcularCusto() {
        double custo = salaPremium.calcularCusto(2);
        assertEquals(2 * Sala.VALOR_BASE_HORA * 1.15, custo, 0.01);
    }

    @Test
    void testCalcularReembolso() {
        double valor = 200.0;
        double reembolso = salaPremium.calcularReembolso(valor);
        assertEquals(valor * 0.4, reembolso, 0.01);
    }
}