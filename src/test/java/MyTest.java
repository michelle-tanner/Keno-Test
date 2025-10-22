import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	@Test
	void test() {
//		fail("Not yet implemented");
	}

    @Test
    void test1() {
        int num = 2;
        Assertions.assertEquals(num, 2, "This should succeed!");
    }

}
