package io.github.fenglar.hello;

import io.github.fenglar.lang.Lang;
import io.github.fenglar.lang.LangRepository;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelloServiceTest {
    private final static String WELCOME = "Hello";
    private final static String FALLBACK_ID_WELCOME = "Hola";


    @Test
    public void test_prepareGreeting_nullName_returnsGreetingWithFallbackName() {
        //given plus when

        var mockRepository = alwaysReturningHelloRepository();
        var SUT = new HelloService(mockRepository);

        var result = SUT.prepareGreeting(null, -1);
        //when

        assertEquals(WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }


    @Test
    public void test_prepareGreeting_name_returnsGreetingWithName() {
        //given
        var SUT = new HelloService();

        String name = "test";
        // when
        var result = SUT.prepareGreeting(name, -1);
        //when

        assertEquals("Hello " + name + "!", result);
    }

    @Test
    public void test_prepareGreeting_nullLang_returnsGreetingWithFallbackLang() {
        //given plus when
        var mockRepository = fallbackLangIdRepository();
        var SUT = new HelloService(mockRepository);

        var result = SUT.prepareGreeting(null, null);
        //when

        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    /*@Test
    public void test_prepareGreeting_textLang_returnsGreetingWithFallbackLang() {
        //given plus when
        var mockRepository = fallbackLangIdRepository();
        var SUT = new HelloService(mockRepository);

        var result = SUT.prepareGreeting(null, "abc");
        //when

        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }
*/

    @Test
    public void test_prepareGreeting_nonExistingLang_returnsGreetingWithFallbackLang() {
        //given plus when
        var mockRepository = new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                return Optional.empty();
            }
        };
        var SUT = new HelloService(mockRepository);

        var result = SUT.prepareGreeting(null, -1);
        //when

        assertEquals(HelloService.FALLBACK_LANG.getWelcomeMsg() + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    private LangRepository fallbackLangIdRepository() {
        return new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                if (id.equals(HelloService.FALLBACK_LANG.getId())) {
                    return Optional.of(new Lang(null, FALLBACK_ID_WELCOME, null));
                }
                return Optional.empty();
            }
        };
    }

    private LangRepository alwaysReturningHelloRepository() {
        return new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                return Optional.of(new Lang(null, WELCOME, null));
            }
        };
    }
}