import org.junit.jupiter.api.Test;

import model.SalaRecurso;

import static org.junit.jupiter.api.Assertions.*;

public class SalaRecursoTest {

    @Test
    void testConstrutorEGetters() {
        SalaRecurso salaRecurso = new SalaRecurso("Projetor", "Projetor 4K com HDMI");

        assertEquals("Projetor", salaRecurso.getNome());
        assertEquals("Projetor 4K com HDMI", salaRecurso.getDescricao());
        assertTrue(salaRecurso.getCodigo() > 0);
    }

    @Test
    void testToString() {
        SalaRecurso salaRecurso = new SalaRecurso("Lousa Digital", "Permite escrita com caneta especial");
        String esperado = "Lousa Digital: Permite escrita com caneta especial";
        assertEquals(esperado, salaRecurso.toString());
    }
}