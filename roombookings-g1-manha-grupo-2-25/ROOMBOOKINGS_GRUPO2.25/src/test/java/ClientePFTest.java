import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientePFTest {

    private ClientePF clientePF;

    @BeforeEach
    void setUp() {
        clientePF = new ClientePF("João Silva", "123.456.789-00");
    }

    @Test
    void testIsCorporativo() {
        assertFalse(clientePF.isCorporativo(), "Cliente pessoa física deve retornar false para isCorporativo");
    }

    @Test
    void testToString() {
        String resultado = clientePF.toString();
        assertEquals("João Silva | CPF: 123.456.789-00 | Pessoa Física", resultado);
    }

    @Test
    void testGetNome() {
        assertEquals("João Silva", clientePF.getNome());
    }

    @Test
    void testGetDocumento() {
        assertEquals("123.456.789-00", clientePF.getDocumento());
    }
}