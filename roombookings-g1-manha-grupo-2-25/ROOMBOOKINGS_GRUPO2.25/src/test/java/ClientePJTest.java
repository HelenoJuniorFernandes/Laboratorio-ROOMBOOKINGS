import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientePJTest {

    private ClientePJ clientePJ;

    @BeforeEach
    void setUp() {
        clientePJ = new ClientePJ("Empresa XYZ", "12.345.678/0001-90");
    }

    @Test
    void testIsCorporativo() {
        assertTrue(clientePJ.isCorporativo(), "Cliente pessoa jurídica deve retornar true para isCorporativo");
    }

    @Test
    void testToString() {
        String resultado = clientePJ.toString();
        assertEquals("Empresa XYZ | CNPJ: 12.345.678/0001-90 | Pessoa Jurídica", resultado);
    }

    @Test
    void testGetNome() {
        assertEquals("Empresa XYZ", clientePJ.getNome());
    }

    @Test
    void testGetDocumento() {
        assertEquals("12.345.678/0001-90", clientePJ.getDocumento());
    }
}