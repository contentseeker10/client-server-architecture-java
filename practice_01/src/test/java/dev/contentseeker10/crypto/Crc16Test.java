package dev.contentseeker10.crypto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Crc16Test {

    private static String STANDARD_TEST_STRING = "123456789";
    private static final short EXPECTED_STANDARD_CRC = (short) 0xBB3D;

    @Test
    void shouldCalculateCrc() {
        short result = Crc16.calculateCrc(STANDARD_TEST_STRING);
        assertThat(result).isEqualTo(EXPECTED_STANDARD_CRC);
    }

    @Test
    void shouldCalculateDifferentCrc() {
        STANDARD_TEST_STRING = "12345";
        short result = Crc16.calculateCrc(STANDARD_TEST_STRING);
        assertThat(result).isNotEqualTo(EXPECTED_STANDARD_CRC);
    }

}