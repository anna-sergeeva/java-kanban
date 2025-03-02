package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    public void testDefaultManagersNotNull() {
        assertNotNull(Managers.getDefault(), "default manager не должен быть равен null");
        assertNotNull(Managers.getDefaultHistory(), "default history managers не должен быть равен null");
    }
}
