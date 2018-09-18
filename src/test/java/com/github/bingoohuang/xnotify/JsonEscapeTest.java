package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.util.JsonEscape;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class JsonEscapeTest {
    @Test
    public void test() {
        assertThat(JsonEscape.escapeJson("{^name^:^bingoo^}")).isEqualTo("{\"name\":\"bingoo\"}");
        assertThat(JsonEscape.escapeJson("{'name':'bingoo'}")).isEqualTo("{\"name\":\"bingoo\"}");
        assertThat(JsonEscape.escapeJson("{`name`:`bingoo`}")).isEqualTo("{\"name\":\"bingoo\"}");
        assertThat(JsonEscape.escapeJson("{\"name\":\"bingoo\"}")).isEqualTo("{\"name\":\"bingoo\"}");
    }
}
