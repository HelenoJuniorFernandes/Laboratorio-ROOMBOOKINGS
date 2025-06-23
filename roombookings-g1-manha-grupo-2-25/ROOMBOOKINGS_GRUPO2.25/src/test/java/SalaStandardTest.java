import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.SalaRecurso;
import model.Sala;

import static org.junit.jupiter.api.Assertions.*;

public class SalaStandardTest {

    private SalaStandard salaStandard;

    @BeforeEach
    void setUp() {
        salaStandard = new SalaStandard(15);
        salaStandard.setCodigo(201);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(201, salaStandard.getCodigo());
        assertEquals(15, salaStandard.getCapacidade());
        assertNotNull(salaStandard.getRecursos());
        assertEquals(0, salaStandard.getRecursos().size());
    }

    @Test
    void testAdicionarRecurso() {
        SalaRecurso salaRecursoExtra = new SalaRecurso("Quadro Branco", "Quadro branco para anotações");
        salaStandard.adicionarRecurso(salaRecursoExtra);

        assertEquals(1, salaStandard.getRecursos().size());
        assertTrue(salaStandard.getRecursos().contains(salaRecursoExtra));
    }

    @Test
    void testToStringComRecursos() {
        SalaRecurso salaRecursoExtra = new SalaRecurso("Ar-condicionado", "Ar-condicionado de última geração");
        salaStandard.adicionarRecurso(salaRecursoExtra);

        String resultado = salaStandard.toString();
        assertTrue(resultado.contains("Sala Standard"));
        assertTrue(resultado.contains("Ar-condicionado: Ar-condicionado de última geração"));
    }

    @Test
    void testCalcularCusto() {
        double custo = salaStandard.calcularCusto(3);
        assertEquals(3 * Sala.VALOR_BASE_HORA, custo, 0.01);
    }

    @Test
    void testCalcularReembolso() {
        double valor = 200.0;
        double reembolso = salaStandard.calcularReembolso(valor);
        assertEquals(valor * 0.6, reembolso, 0.01);
    }
}