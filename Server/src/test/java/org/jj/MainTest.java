package org.jj;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class MainTest {
    @Test
    void name() {
        List<String> list = Mockito.mock(List.class);

        when(list.get(anyInt())).thenReturn("JENSEN");
        assertThat(list.get(1)).isEqualTo("JENSEN");
    }
}