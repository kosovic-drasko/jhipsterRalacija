package relacija.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import relacija.web.rest.TestUtil;

class PonudjaciTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ponudjaci.class);
        Ponudjaci ponudjaci1 = new Ponudjaci();
        ponudjaci1.setId(1L);
        Ponudjaci ponudjaci2 = new Ponudjaci();
        ponudjaci2.setId(ponudjaci1.getId());
        assertThat(ponudjaci1).isEqualTo(ponudjaci2);
        ponudjaci2.setId(2L);
        assertThat(ponudjaci1).isNotEqualTo(ponudjaci2);
        ponudjaci1.setId(null);
        assertThat(ponudjaci1).isNotEqualTo(ponudjaci2);
    }
}
